package com.acimage.user.service.user.impl;


import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.acimage.common.global.consts.JwtConstants;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.global.exception.BusinessException;

import com.acimage.common.global.consts.HeaderKeyConstants;
import com.acimage.common.model.domain.community.CmtyUser;
import com.acimage.common.model.domain.user.User;
import com.acimage.common.service.TokenService;
import com.acimage.common.utils.IdGenerator;
import com.acimage.common.utils.SensitiveWordUtils;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.user.dao.UserDao;
import com.acimage.user.dao.UserPrivacyDao;
import com.acimage.common.model.domain.user.UserPrivacy;
import com.acimage.user.model.request.UserLoginReq;
import com.acimage.user.model.request.UserRegisterReq;
import com.acimage.user.mq.producer.SyncUserMqProducer;
import com.acimage.user.service.user.LoginService;
import com.acimage.common.utils.RsaUtils;
import com.acimage.user.service.usermsg.UserMsgWriteService;
import com.acimage.user.service.verify.VerifyCodeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    public static final String PASSWORD_PATTERN = "^(?![a-zA-Z]+$)[0-9A-Za-z\\W]{6,16}$";

    public static final String STRINGKP_SEND_EMAIL = "acimage:user:logins:sendEmailCode:email:";

    @Autowired
    UserDao userDao;
    @Autowired
    UserPrivacyDao userPrivacyDao;
    @Autowired
    UserMsgWriteService userMsgWriteService;
    @Autowired
    TokenService tokenService;
    @Autowired
    SyncUserMqProducer syncUserMqProducer;
    @Autowired
    VerifyCodeService verifyCodeService;
    @Autowired
    RedisUtils redisUtils;

    @Override
    public String getPublicKey() {
        return RsaUtils.getPublicKey();
    }

    @Override
    public String registerUser(UserRegisterReq userRegister) {
        //????????????
        String privateKey = RsaUtils.getPrivateKey();
        //?????????????????????
        String passwordDecrypt = RsaUtils.decrypt(privateKey, userRegister.getPassword());
//        log.info(" ????????????{}", passwordDecrypt);
        if (!Pattern.matches(PASSWORD_PATTERN, passwordDecrypt)) {
            throw new BusinessException("???????????????6???16?????????????????????????????????????????????");
        }

        //??????username????????????
        String username = SensitiveWordUtils.filter(userRegister.getUsername());
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        qw.eq(User::getUsername, username);
        User userByUsername = userDao.selectOne(qw);
        if (userByUsername != null) {
            log.info("???????????? ?????? ??????????????????{}?????????", username);
            throw new BusinessException("??????????????????");
        }

        //??????email????????????
        String email = userRegister.getEmail();
        LambdaQueryWrapper<UserPrivacy> qwForEmail = new LambdaQueryWrapper<>();
        qwForEmail.eq(UserPrivacy::getEmail, email);
        UserPrivacy userByEmail = userPrivacyDao.selectOne(qwForEmail);
        if (userByEmail != null) {
            log.warn("???????????? error?????????{}?????????", email);
            throw new BusinessException("email?????????");
        }

        //???????????????
        String salt = IdUtil.simpleUUID();
        log.debug("???????????? {}", salt);
        //??????????????????????????????????????????
        String passwordDigest = DigestUtil.md5Hex(salt + passwordDecrypt);
        log.debug(" md5????????????{}", passwordDigest);

        //????????????id
        long userId = IdGenerator.getSnowflakeNextId();
        log.debug("??????????????????id??????{}", userId);

        //????????????
        User insertedUser = new User(userId, username);
        userDao.insert(insertedUser);
        UserPrivacy userPrivacy = new UserPrivacy(userId, passwordDigest, salt, email);
        userPrivacyDao.insert(userPrivacy);
        userMsgWriteService.insert(userId);


        //??????token
        String defaultPhotoUrl = "";
        //mq???????????????????????????
        CmtyUser cmtyUser = new CmtyUser();
        cmtyUser.setId(userId);
        cmtyUser.setUsername(username);
        cmtyUser.setPhotoUrl(defaultPhotoUrl);
        syncUserMqProducer.sendAddUserMessage(cmtyUser);

        return tokenService.createAndRecordToken(userId, username, defaultPhotoUrl, JwtConstants.USER_EXPIRE_DAYS);

    }

    @Override
    public String login(UserLoginReq userLogin) {
        String email = userLogin.getEmail();
        String password = userLogin.getPassword();

        LambdaQueryWrapper<UserPrivacy> qw = new LambdaQueryWrapper<>();
        qw.eq(UserPrivacy::getEmail, email);
        UserPrivacy userPrivacy = userPrivacyDao.selectOne(qw);
        if (userPrivacy == null) {
            throw new BusinessException("???????????????");
        }

        //????????????
        long userId = userPrivacy.getUserId();
        String salt = userPrivacy.getSalt();
        String passwordDigest = userPrivacy.getPwd();

        //????????????
        String privateKey = RsaUtils.getPrivateKey();
        //????????????
        String passwordDecrypt = RsaUtils.decrypt(privateKey, password);
//        log.info(" ????????????{}", passwordDecrypt);
        //?????????????????????
        if (!DigestUtil.md5Hex(salt + passwordDecrypt).equals(passwordDigest)) {
            log.warn("?????? ???????????????{} ???????????????", email);
            throw new BusinessException("????????????????????????");
        }

        //??????token
        User user = userDao.selectById(userId);
        return tokenService.createAndRecordToken(userId, user.getUsername(), user.getPhotoUrl(), JwtConstants.USER_EXPIRE_DAYS);

    }


    @Override
    public void logout(HttpServletRequest request) {
        //??????cookie??????token
        String token = request.getHeader(HeaderKeyConstants.AUTHORIZATION);
        tokenService.invalidate(token);
    }

    @Override
    public void checkAndSendCodeToEmail(String email) {
        if (redisUtils.getForString(STRINGKP_SEND_EMAIL + email) != null) {
            log.info("ip:{} ??????????????????????????????", UserContext.getIp());
            throw new BusinessException("email??????????????????????????????30s???????????????");
        }

        //????????????????????????
        LambdaQueryWrapper<UserPrivacy> qw = new LambdaQueryWrapper<>();
        qw.select(UserPrivacy::getEmail).eq(UserPrivacy::getEmail, email);
        if (userPrivacyDao.selectOne(qw) != null) {
            throw new BusinessException("???????????????");
        }
        verifyCodeService.sendVerifyCodeToEmail(email, UserRegisterReq.VERIFY_CODE_LENGTH);
        long timeout = 30;

        redisUtils.setAsString(STRINGKP_SEND_EMAIL + email, "0", timeout, TimeUnit.SECONDS);
    }


}

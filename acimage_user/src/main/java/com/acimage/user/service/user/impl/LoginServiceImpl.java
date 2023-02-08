package com.acimage.user.service.user.impl;


import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.acimage.common.exception.BusinessException;

import com.acimage.common.global.consts.HeaderKeyConstants;
import com.acimage.common.model.domain.community.CmtyUser;
import com.acimage.common.model.domain.user.User;
import com.acimage.common.service.TokenService;
import com.acimage.common.utils.IdGenerator;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.user.dao.UserDao;
import com.acimage.user.dao.UserPrivacyDao;
import com.acimage.common.model.domain.user.UserPrivacy;
import com.acimage.user.model.request.UserLoginReq;
import com.acimage.user.model.request.UserRegisterReq;
import com.acimage.user.mq.producer.SyncUserMqProducer;
import com.acimage.user.service.user.LoginService;
import com.acimage.common.utils.RsaUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    public static final String PASSWORD_PATTERN = "^(?![a-zA-Z]+$)[0-9A-Za-z\\W]{6,16}$";

    @Autowired
    UserDao userDao;
    @Autowired
    UserPrivacyDao userPrivacyDao;
    @Autowired
    TokenService tokenService;
    @Autowired
    SyncUserMqProducer syncUserMqProducer;
    @Autowired
    RedisUtils redisUtils;

    @Override
    public String getPublicKey() {
        return RsaUtils.getPublicKey();
    }

    @Override
    public String registerUser(UserRegisterReq userRegister) {
        //获取私钥
        String privateKey = RsaUtils.getPrivateKey();
        //解密并校验密码
        String passwordDecrypt = RsaUtils.decrypt(privateKey, userRegister.getPassword());
//        log.info(" 解密为：{}", passwordDecrypt);
        if (!Pattern.matches(PASSWORD_PATTERN, passwordDecrypt)) {
            throw new BusinessException("密码长度为6至16位，且只含数字、字母和特殊字符");
        }

        //判断username是否存在
        String username = userRegister.getUsername();
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        qw.eq(User::getUsername, username);
        User userByUsername = userDao.selectOne(qw);
        if (userByUsername != null) {
            log.info("用户：无 注册 错误：用户名{}已存在", username);
            throw new BusinessException("用户名已存在");
        }

        //判断email是否存在
        String email = userRegister.getEmail();
        LambdaQueryWrapper<UserPrivacy> qwForEmail = new LambdaQueryWrapper<>();
        qwForEmail.eq(UserPrivacy::getEmail, email);
        UserPrivacy userByEmail = userPrivacyDao.selectOne(qwForEmail);
        if (userByEmail != null) {
            log.warn("用户注册 error：邮箱{}已存在", email);
            throw new BusinessException("email已存在");
        }

        //生成随机盐
        String salt = IdUtil.simpleUUID();
        log.info("随机盐为 {}", salt);
        //利用随机盐进行密钥摘要或加密
        String passwordDigest = DigestUtil.md5Hex(salt + passwordDecrypt);
        log.info(" md5摘要为：{}", passwordDigest);

        //生成用户id
        long userId = IdGenerator.getSnowflakeNextId();
        log.info(" 雪花算法生成id为：{}", userId);

        //插入用户
        User insertedUser = new User(userId, username);
        userDao.insert(insertedUser);
        UserPrivacy userPrivacy = new UserPrivacy(userId, passwordDigest, salt, email);
        userPrivacyDao.insert(userPrivacy);

        //返回token
        String defaultPhotoUrl = "";
        //mq发送消息，同步数据
        CmtyUser cmtyUser=new CmtyUser();
        cmtyUser.setId(userId);
        cmtyUser.setUsername(username);
        cmtyUser.setPhotoUrl(defaultPhotoUrl);
        syncUserMqProducer.sendAddUserMessage(cmtyUser);

        return tokenService.createAndRecordToken(userId, username, defaultPhotoUrl);

    }

    @Override
    public String login(UserLoginReq userLogin) {
        String email = userLogin.getEmail();
        String password = userLogin.getPassword();

        LambdaQueryWrapper<UserPrivacy> qw = new LambdaQueryWrapper<>();
        qw.eq(UserPrivacy::getEmail, email);
        UserPrivacy userPrivacy = userPrivacyDao.selectOne(qw);
        if (userPrivacy == null) {
            throw new BusinessException("邮箱不存在");
        }

        //找到密码
        long userId=userPrivacy.getUserId();
        String salt = userPrivacy.getSalt();
        String passwordDigest = userPrivacy.getPwd();

        //获取私钥
        String privateKey = RsaUtils.getPrivateKey();
        //解密密码
        String passwordDecrypt = RsaUtils.decrypt(privateKey, password);
//        log.info(" 解密为：{}", passwordDecrypt);
        //判断密码正确性
        if (!DigestUtil.md5Hex(salt + passwordDecrypt).equals(passwordDigest)) {
            log.warn("登录 错误：邮箱{} 或密码错误", email);
            throw new BusinessException("用户名或密码错误");
        }

        //返回token
        User user=userDao.selectById(userId);
        return tokenService.createAndRecordToken(userId, user.getUsername(), user.getPhotoUrl());

    }



    @Override
    public void logout(HttpServletRequest request) {
        //获取cookie中的token
        String token = request.getHeader(HeaderKeyConstants.AUTHORIZATION);
        tokenService.invalidate(token);
    }


}

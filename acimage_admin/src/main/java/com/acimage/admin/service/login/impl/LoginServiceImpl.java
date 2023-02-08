package com.acimage.admin.service.login.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.acimage.admin.dao.user.UserDao;
import com.acimage.admin.dao.user.UserPrivacyDao;
import com.acimage.admin.global.consts.ModuleConstants;
import com.acimage.admin.model.request.LoginReq;
import com.acimage.admin.service.login.LoginService;
import com.acimage.common.exception.BusinessException;
import com.acimage.common.model.domain.user.User;
import com.acimage.common.model.domain.user.UserPrivacy;
import com.acimage.common.service.TokenService;
import com.acimage.common.utils.RsaUtils;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@DS(ModuleConstants.USER)
public class LoginServiceImpl implements LoginService {
    @Autowired
    UserPrivacyDao userPrivacyDao;
    @Autowired
    UserDao userDao;
    @Autowired
    TokenService tokenService;

    @Override
    public String login(LoginReq loginReq) {
        String email = loginReq.getEmail();
        String password = loginReq.getPassword();

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
//        log.debug(" decrypt as：{}", passwordDecrypt);
        //判断密码正确性
        if (!DigestUtil.md5Hex(salt + passwordDecrypt).equals(passwordDigest)) {
            log.warn("登录 错误：邮箱{} 或密码错误", email);
            throw new BusinessException("用户名或密码错误");
        }

        //返回token
        User user=userDao.selectById(userId);
        return tokenService.createAndRecordToken(userId, user.getUsername(), user.getPhotoUrl());

    }
}

package com.acimage.user.service.user.impl;


import cn.hutool.core.util.StrUtil;
import com.acimage.common.global.consts.JwtConstants;
import com.acimage.common.global.context.UserContext;

import com.acimage.common.model.domain.user.User;
import com.acimage.common.model.mq.dto.UserIdWithPhotoUrl;
import com.acimage.common.model.mq.dto.UserIdWithUsername;
import com.acimage.common.utils.SensitiveWordUtils;
import com.acimage.common.utils.common.FileUtils;
import com.acimage.common.utils.IdGenerator;
import com.acimage.common.deprecated.QiniuUtils;
import com.acimage.common.service.TokenService;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.user.dao.UserDao;
import com.acimage.user.global.consts.StorePrefixConst;
import com.acimage.user.mq.producer.SyncUserMqProducer;
import com.acimage.user.service.user.UserWriteService;
import com.acimage.user.service.user.consts.KeyConstants;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;


@Slf4j
@Service
public class UserWriteServiceImpl implements UserWriteService {

    @Autowired
    UserDao userDao;
    @Autowired
    TokenService tokenService;
    @Autowired
    RedisUtils redisUtils;
    @Autowired(required = false)
    QiniuUtils qiniuUtils;
    @Autowired
    SyncUserMqProducer syncUserMqProducer;

    @Override
    public String updateUsername(String username) {
        String filterUsername=SensitiveWordUtils.filter(username);
        LambdaUpdateWrapper<User> qw = new LambdaUpdateWrapper<>();
        qw.eq(User::getId, UserContext.getUserId())
                .set(User::getUsername, filterUsername);
        userDao.update(null, qw);

        String token = tokenService.createAndRecordToken(UserContext.getUserId(),
                filterUsername,
                UserContext.getPhotoUrl(),
                JwtConstants.USER_EXPIRE_DAYS);
        //??????redis??????
        String key = KeyConstants.STRINGKP_USER + UserContext.getUserId();
        redisUtils.delete(key);

        //mq???????????????????????????
        syncUserMqProducer.sendSyncUsernameMessage(new UserIdWithUsername(UserContext.getUserId(),filterUsername));
        return token;
    }

    @Override
    public String uploadPhotoAndUpdatePhotoUrl(MultipartFile photoFile) {
        //????????????????????????
        Date now = new Date();
        String format = FileUtils.formatOf(photoFile.getOriginalFilename());
        String suffix = String.format("%s.%s", IdGenerator.getSnowflakeNextId(), format);
        String newPhotoUrl = qiniuUtils.generateUrl(suffix, now, StorePrefixConst.USER_PHOTO);

        //??????photoUrl
        LambdaUpdateWrapper<User> qw = new LambdaUpdateWrapper<>();
        qw.set(User::getPhotoUrl, newPhotoUrl)
                .eq(User::getId, UserContext.getUserId());
        userDao.update(null, qw);

        qiniuUtils.upload(photoFile, newPhotoUrl);
        //???????????????
        if (!StrUtil.isEmpty(UserContext.getPhotoUrl())) {
            qiniuUtils.deleteFile(UserContext.getPhotoUrl());
        }

        //???????????????
        String token = tokenService.createAndRecordToken(UserContext.getUserId(),
                UserContext.getUsername(),
                newPhotoUrl,
                JwtConstants.USER_EXPIRE_DAYS);

        //??????redis??????
        String key = KeyConstants.STRINGKP_USER + UserContext.getUserId();
        redisUtils.delete(key);

        return token;
    }

    @Override
    public String updatePhotoUrl(String newPhotoUrl) {
        //??????photoUrl
        LambdaUpdateWrapper<User> qw = new LambdaUpdateWrapper<>();
        qw.set(User::getPhotoUrl, newPhotoUrl)
                .eq(User::getId, UserContext.getUserId());
        userDao.update(null, qw);

        //?????????????????????
        String key = KeyConstants.STRINGKP_USER + UserContext.getUserId();
        redisUtils.delete(key);

        //mq??????????????????????????????
        syncUserMqProducer.sendSyncUserPhotoUrlMessage(new UserIdWithPhotoUrl(UserContext.getUserId(),newPhotoUrl));
        //?????????token
        return tokenService.createAndRecordToken(UserContext.getUserId(),
                UserContext.getUsername(),
                newPhotoUrl,
                JwtConstants.USER_EXPIRE_DAYS);

    }
}

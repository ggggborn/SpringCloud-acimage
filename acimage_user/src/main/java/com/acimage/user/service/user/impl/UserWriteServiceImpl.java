package com.acimage.user.service.user.impl;


import cn.hutool.core.util.StrUtil;
import com.acimage.common.global.context.UserContext;

import com.acimage.common.model.domain.User;
import com.acimage.common.model.mq.dto.UserIdWithPhotoUrl;
import com.acimage.common.model.mq.dto.UserIdWithUsername;
import com.acimage.common.utils.common.FileUtils;
import com.acimage.common.utils.IdGenerator;
import com.acimage.common.utils.QiniuUtils;
import com.acimage.common.service.TokenService;
import com.acimage.common.utils.RedisUtils;
import com.acimage.user.dao.UserDao;
import com.acimage.user.global.consts.StorePrefixConst;
import com.acimage.user.mq.produce.SyncUserMqProducer;
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
    @Autowired
    QiniuUtils qiniuUtils;
    @Autowired
    SyncUserMqProducer syncUserMqProducer;

    @Override
    public String updateUsername(String username, HttpServletResponse resp) {
        LambdaUpdateWrapper<User> qw = new LambdaUpdateWrapper<>();
        qw.eq(User::getId, UserContext.getUserId())
                .set(User::getUsername, username);
        userDao.update(null, qw);

        String token = tokenService.createToken(UserContext.getUserId(), username, UserContext.getPhotoUrl());
        //删除redis数据
        String key = KeyConstants.STRINGKP_USER + UserContext.getUserId();
        redisUtils.delete(key);

        //mq发送同步用户名消息
        syncUserMqProducer.sendSyncUsernameMessage(new UserIdWithUsername(UserContext.getUserId(),username));
        return token;
    }

    @Override
    public String uploadPhotoAndUpdatePhotoUrl(MultipartFile photoFile, HttpServletResponse resp) {
        //上传头像到七牛云
        Date now = new Date();
        String format = FileUtils.formatOf(photoFile.getOriginalFilename());
        String suffix = String.format("%s.%s", IdGenerator.getSnowflakeNextId(), format);
        String newPhotoUrl = qiniuUtils.generateUrl(suffix, now, StorePrefixConst.USER_PHOTO);

        //更新photoUrl
        LambdaUpdateWrapper<User> qw = new LambdaUpdateWrapper<>();
        qw.set(User::getPhotoUrl, newPhotoUrl)
                .eq(User::getId, UserContext.getUserId());
        userDao.update(null, qw);

        qiniuUtils.upload(photoFile, newPhotoUrl);
        //删除旧头像
        if (!StrUtil.isEmpty(UserContext.getPhotoUrl())) {
            qiniuUtils.deleteFile(UserContext.getPhotoUrl());
        }

        //返回新凭证
        String token = tokenService.createToken(UserContext.getUserId(), UserContext.getUsername(), newPhotoUrl);

        //删除redis数据
        String key = KeyConstants.STRINGKP_USER + UserContext.getUserId();
        redisUtils.delete(key);

        return token;
    }

    @Override
    public String updatePhotoUrl(String newPhotoUrl) {
        //更新photoUrl
        LambdaUpdateWrapper<User> qw = new LambdaUpdateWrapper<>();
        qw.set(User::getPhotoUrl, newPhotoUrl)
                .eq(User::getId, UserContext.getUserId());
        userDao.update(null, qw);

        //删除缓存的数据
        String key = KeyConstants.STRINGKP_USER + UserContext.getUserId();
        redisUtils.delete(key);

        //mq发送同步用户头像消息
        syncUserMqProducer.sendSyncUserPhotoUrlMessage(new UserIdWithPhotoUrl(UserContext.getUserId(),newPhotoUrl));
        //返回新token
        return tokenService.createToken(UserContext.getUserId(), UserContext.getUsername(), newPhotoUrl);

    }
}

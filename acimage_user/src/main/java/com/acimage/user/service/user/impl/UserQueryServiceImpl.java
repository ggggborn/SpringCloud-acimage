package com.acimage.user.service.user.impl;

import com.acimage.common.global.exception.BusinessException;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.model.domain.user.User;
import com.acimage.common.redis.annotation.KeyParam;
import com.acimage.common.redis.annotation.QueryRedis;

import com.acimage.user.dao.UserDao;
import com.acimage.user.service.user.UserQueryService;
import com.acimage.user.service.user.consts.KeyConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserQueryServiceImpl implements UserQueryService {
    @Autowired
    UserDao userDao;

    @QueryRedis(keyPrefix = KeyConstants.STRINGKP_USER, expire = 37L)
    @Override
    public User getUser(@KeyParam long userId) {
        User user = userDao.selectById(userId);
        if (user == null) {
            log.error("用户:{} 查询 用户{} 错误：用户不存在", UserContext.getUsername(), userId);
            throw new BusinessException("该用户不存在");
        }
        return user;
    }

    @QueryRedis(keyPrefix = KeyConstants.STRINGKP_USERNAME, expire = 17L, unit = TimeUnit.SECONDS)
    @Override
    public Boolean isUsernameExist(@KeyParam String username) {
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        qw.eq(User::getUsername, username);
        return userDao.selectOne(qw) != null;
    }
}

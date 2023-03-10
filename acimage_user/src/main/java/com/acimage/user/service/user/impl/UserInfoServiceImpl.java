package com.acimage.user.service.user.impl;

import com.acimage.common.global.context.UserContext;
import com.acimage.common.model.domain.community.CmtyUser;

import com.acimage.common.redis.annotation.QueryRedis;

import com.acimage.feign.client.CmtyUserClient;
import com.acimage.user.dao.UserDao;
import com.acimage.user.dao.UserPrivacyDao;
import com.acimage.common.model.domain.user.UserPrivacy;
import com.acimage.user.model.vo.ProfileVo;
import com.acimage.user.service.user.UserInfoService;
import com.acimage.user.service.user.UserQueryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    UserQueryService userQueryService;
    @Autowired
    UserDao userDao;
    @Autowired
    UserPrivacyDao userPrivacyDao;
    @Autowired
    CmtyUserClient cmtyUserClient;

    @QueryRedis(keyPrefix = "acimage:users:profile:userId:", expire = 2L, unit = TimeUnit.SECONDS)
    @Override
    public ProfileVo getProfile() {
        CmtyUser cmtyUser = cmtyUserClient.queryCmtyUser(UserContext.getUserId()).getData();

        ProfileVo profileVo = new ProfileVo();
        profileVo.setStarCount(cmtyUser.getStarCount());
        profileVo.setTopicCount(cmtyUser.getTopicCount());

        LambdaQueryWrapper<UserPrivacy> qw = new LambdaQueryWrapper<>();
        qw.eq(UserPrivacy::getUserId, UserContext.getUserId());
        UserPrivacy userPrivacy = userPrivacyDao.selectOne(qw);

        profileVo.setEmail(userPrivacy.getEmail());
        profileVo.setRegisterTime(userPrivacy.getRegisterTime());
        profileVo.setUsername(UserContext.getUsername());

        return profileVo;
    }

}

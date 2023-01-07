package com.acimage.user.service.user.impl;

import cn.hutool.core.bean.BeanUtil;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.model.domain.User;
import com.acimage.common.model.domain.UserCommunityStatistic;

import com.acimage.common.redis.annotation.QueryRedis;

import com.acimage.feign.client.UserCommunityStatisticClient;
import com.acimage.user.dao.UserDao;
import com.acimage.user.dao.UserPrivacyDao;
import com.acimage.user.model.domain.UserPrivacy;
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
    UserCommunityStatisticClient userCsClient;
//    @Autowired
//    StarQueryService starQueryService;

    @QueryRedis(keyPrefix = "acimage:users:profile:userId:", expire = 2L, unit = TimeUnit.SECONDS)
    @Override
    public ProfileVo getProfile() {
        UserCommunityStatistic userStatistic = userCsClient.queryUserCommunityStatistic(UserContext.getUserId()).getData();

        ProfileVo profileVo = new ProfileVo();
        profileVo.setStarCount(userStatistic.getStarCount());
        profileVo.setTopicCount(userStatistic.getTopicCount());

        LambdaQueryWrapper<UserPrivacy> qw = new LambdaQueryWrapper<>();
        qw.eq(UserPrivacy::getId, UserContext.getUserId());
        UserPrivacy userPrivacy = userPrivacyDao.selectOne(qw);

        profileVo.setEmail(userPrivacy.getEmail());
        profileVo.setRegisterTime(userPrivacy.getRegisterTime());
        profileVo.setUsername(UserContext.getUsername());

        return profileVo;
    }


    @Override
    public User getUserWithStarCount(long userId) {

        User user = userQueryService.getUser(userId);
        if (user != null) {
//            user.setStarCount(starQueryService.getStarCountOwnedBy(userId));
        }
        return user;
    }
}

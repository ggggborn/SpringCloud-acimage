package com.acimage.community.service.userstatistic.impl;

import com.acimage.common.model.domain.UserCommunityStatistic;
import com.acimage.common.redis.annotation.QueryRedis;
import com.acimage.community.dao.UserCommunityStatisticDao;
import com.acimage.community.service.userstatistic.UserCsQueryService;
import com.acimage.community.service.userstatistic.consts.KeyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCsQueryServiceImpl implements UserCsQueryService {
    @Autowired
    UserCommunityStatisticDao userCsDao;

    @Override
    @QueryRedis(keyPrefix = KeyConstants.STRINGKP_USER_STATISTIC,expire = 10L)
    public UserCommunityStatistic getUserCommunityStatistic(long userId){
        return userCsDao.selectById(userId);

    }
}

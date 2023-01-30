package com.acimage.community.service.userstatistic.impl;

import com.acimage.common.global.UserCommunityStatistic;
import com.acimage.common.redis.annotation.QueryRedis;
import com.acimage.common.redis.enums.DataType;
import com.acimage.community.global.CmtyUserDaoBak;
import com.acimage.community.service.userstatistic.UserCsQueryService;
import com.acimage.community.service.userstatistic.consts.KeyConstants;
import org.springframework.beans.factory.annotation.Autowired;


public class UserCsQueryServiceImpl implements UserCsQueryService {
    @Autowired
    CmtyUserDaoBak userCsDao;

    @Override
    @QueryRedis(keyPrefix = KeyConstants.STRINGKP_USER_STATISTIC, expire = 10L, dataType = DataType.HASH)
    public UserCommunityStatistic getUserCommunityStatistic(long userId) {
        return userCsDao.selectById(userId);
    }
}

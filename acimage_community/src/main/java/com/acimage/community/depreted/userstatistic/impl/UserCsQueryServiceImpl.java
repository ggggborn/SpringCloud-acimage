package com.acimage.community.depreted.userstatistic.impl;

import com.acimage.common.deprecated.UserCommunityStatistic;
import com.acimage.common.redis.annotation.QueryRedis;
import com.acimage.common.redis.enums.DataType;
import com.acimage.community.depreted.CmtyUserDaoBak;
import com.acimage.community.depreted.userstatistic.consts.KeyConstants;
import com.acimage.community.depreted.userstatistic.UserCsQueryService;
import org.springframework.beans.factory.annotation.Autowired;


public class UserCsQueryServiceImpl implements UserCsQueryService {
    @Autowired
    CmtyUserDaoBak userCsDao;

    @Override
    @QueryRedis(keyPrefix = KeyConstants.STRINGKP_CMTY_USER, expire = 10L, dataType = DataType.HASH)
    public UserCommunityStatistic getUserCommunityStatistic(long userId) {
        return userCsDao.selectById(userId);
    }
}

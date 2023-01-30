package com.acimage.community.service.cmtyuser.impl;

import com.acimage.common.model.domain.community.CmtyUser;
import com.acimage.common.redis.annotation.QueryRedis;
import com.acimage.common.redis.enums.DataType;
import com.acimage.community.dao.CmtyUserDao;
import com.acimage.community.service.cmtyuser.CmtyUserQueryService;
import com.acimage.community.service.userstatistic.consts.KeyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CmtyUserQueryServiceImpl implements CmtyUserQueryService {
    @Autowired
    CmtyUserDao cmtyUserDao;

    @Override
    @QueryRedis(keyPrefix = KeyConstants.STRINGKP_USER_STATISTIC, expire = 10L, dataType = DataType.HASH)
    public CmtyUser getUserCommunityStatistic(long userId) {
        return cmtyUserDao.selectById(userId);
    }
}

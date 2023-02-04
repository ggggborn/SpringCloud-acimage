package com.acimage.community.service.cmtyuser.impl;

import com.acimage.common.model.domain.community.CmtyUser;
import com.acimage.common.redis.annotation.QueryRedis;
import com.acimage.common.redis.enums.DataType;
import com.acimage.community.dao.CmtyUserDao;
import com.acimage.community.service.cmtyuser.CmtyUserQueryService;
import com.acimage.community.depreted.userstatistic.consts.KeyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CmtyUserQueryServiceImpl implements CmtyUserQueryService {
    @Autowired
    CmtyUserDao cmtyUserDao;

    @Override
    @QueryRedis(keyPrefix = KeyConstants.STRINGKP_CMTY_USER, expire = 10L, dataType = DataType.HASH)
    public CmtyUser getCmtyUser(long userId) {
        return cmtyUserDao.selectById(userId);
    }
}

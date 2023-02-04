package com.acimage.community.service.cmtyuser;

import com.acimage.common.model.domain.community.CmtyUser;
import com.acimage.common.redis.annotation.QueryRedis;
import com.acimage.common.redis.enums.DataType;
import com.acimage.community.depreted.userstatistic.consts.KeyConstants;

public interface CmtyUserQueryService {
    @QueryRedis(keyPrefix = KeyConstants.STRINGKP_CMTY_USER, expire = 10L, dataType = DataType.HASH)
    CmtyUser getCmtyUser(long userId);
}

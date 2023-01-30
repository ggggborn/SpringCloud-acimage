package com.acimage.community.service.cmtyuser;

import com.acimage.common.model.domain.community.CmtyUser;
import com.acimage.common.redis.annotation.QueryRedis;
import com.acimage.common.redis.enums.DataType;
import com.acimage.community.service.userstatistic.consts.KeyConstants;

public interface CmtyUserQueryService {
    @QueryRedis(keyPrefix = KeyConstants.STRINGKP_USER_STATISTIC, expire = 10L, dataType = DataType.HASH)
    CmtyUser getUserCommunityStatistic(long userId);
}

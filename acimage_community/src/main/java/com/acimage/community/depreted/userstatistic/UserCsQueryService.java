package com.acimage.community.depreted.userstatistic;

import com.acimage.common.deprecated.UserCommunityStatistic;
import com.acimage.common.redis.annotation.QueryRedis;
import com.acimage.community.depreted.userstatistic.consts.KeyConstants;

public interface UserCsQueryService {
    @QueryRedis(keyPrefix = KeyConstants.STRINGKP_CMTY_USER,expire = 10L)
    UserCommunityStatistic getUserCommunityStatistic(long userId);
}

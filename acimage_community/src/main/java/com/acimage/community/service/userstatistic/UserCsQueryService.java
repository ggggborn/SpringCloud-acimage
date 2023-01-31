package com.acimage.community.service.userstatistic;

import com.acimage.common.deprecated.UserCommunityStatistic;
import com.acimage.common.redis.annotation.QueryRedis;
import com.acimage.community.service.userstatistic.consts.KeyConstants;

public interface UserCsQueryService {
    @QueryRedis(keyPrefix = KeyConstants.STRINGKP_USER_STATISTIC,expire = 10L)
    UserCommunityStatistic getUserCommunityStatistic(long userId);
}

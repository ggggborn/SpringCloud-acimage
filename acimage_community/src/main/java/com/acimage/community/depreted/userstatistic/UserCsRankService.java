package com.acimage.community.depreted.userstatistic;

import com.acimage.common.model.domain.user.User;
import com.acimage.common.redis.annotation.QueryRedis;

import java.util.List;

public interface UserCsRankService {

    @QueryRedis(keyPrefix = "acimage:community:users:rank:topicCount",expire = 5L)
    List<User> pageUserRankByTopicCount(int pageNo, int pageSize);

    @QueryRedis(keyPrefix = "acimage:community:users:rank:starCount",expire = 5L)
    List<User> pageUserRankByStarCount(int pageNo, int pageSize);
}

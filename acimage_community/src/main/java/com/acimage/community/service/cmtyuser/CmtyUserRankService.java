package com.acimage.community.service.cmtyuser;

import com.acimage.common.model.domain.community.CmtyUser;
import com.acimage.common.model.page.MyPage;
import com.acimage.common.redis.annotation.QueryRedis;

import java.util.List;

public interface CmtyUserRankService {
    MyPage<CmtyUser> pageUserRankBy(String column, int pageNo, int pageSize);

    @QueryRedis(keyPrefix = "acimage:community:users:rank:topicCount:",expire = 5L)
    List<CmtyUser> pageUserRankByTopicCount(int pageNo, int pageSize);

    @QueryRedis(keyPrefix = "acimage:community:users:rank:starCount:",expire = 5L)
    List<CmtyUser> pageUserRankByStarCount(int pageNo, int pageSize);
}

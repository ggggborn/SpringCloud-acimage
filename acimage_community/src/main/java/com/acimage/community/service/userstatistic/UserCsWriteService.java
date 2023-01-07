package com.acimage.community.service.userstatistic;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserCsWriteService {
    Integer updateStarCountByIncrements(List<Long> userIds, List<Integer> starCounts);

    Integer updateTopicCountByIncrements(List<Long> userIds, List<Integer> starCounts);

    Integer updateTopicCountByIncrement(long userId, int increment);

    Integer updateStarCountByIncrement(long userId, int increment);

    void save(long userId);
}

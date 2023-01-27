package com.acimage.community.dao;

import cn.hutool.core.lang.Pair;
import com.acimage.common.model.domain.community.UserCommunityStatistic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserCommunityStatisticDao extends BaseMapper<UserCommunityStatistic> {
    Integer batchUpdateStarCount(@Param("userIdAndIncrements") List<Pair<Long,Integer>> userIdAndIncrements);
    Integer batchUpdateTopicCount(@Param("userIdAndIncrements") List<Pair<Long,Integer>> userIdAndIncrements);

    @Update("update tb_user_community_statistic set topic_count=topic_count+#{increment} where user_id=#{userId}")
    Integer updateTopicCountByIncrement(@Param("userId") long userId, @Param("increment") int increment);

    @Update("update tb_user_community_statistic set star_count=star_count+#{increment} where user_id=#{userId}")
    Integer updateStarCountByIncrement(@Param("userId") long userId, @Param("increment") int increment);


    List<UserCommunityStatistic> selectListOrderByColumn(@Param("column") String underlineColumnName,
                                                         @Param("startIndex") int startIndex,
                                                         @Param("recordNum") Integer recordNum);
}

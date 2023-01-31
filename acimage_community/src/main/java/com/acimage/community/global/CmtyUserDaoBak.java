package com.acimage.community.global;

import cn.hutool.core.lang.Pair;
import com.acimage.common.deprecated.UserCommunityStatistic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CmtyUserDaoBak extends BaseMapper<UserCommunityStatistic> {
    Integer batchUpdateStarCount(@Param("userIdAndIncrements") List<Pair<Long,Integer>> userIdAndIncrements);
    Integer batchUpdateTopicCount(@Param("userIdAndIncrements") List<Pair<Long,Integer>> userIdAndIncrements);

    @Update("update tb_cmty_user set topic_count=topic_count+#{increment} where user_id=#{userId}")
    Integer updateTopicCountByIncrement(@Param("userId") long userId, @Param("increment") int increment);

    @Update("update tb_cmty_user set star_count=star_count+#{increment} where user_id=#{userId}")
    Integer updateStarCountByIncrement(@Param("userId") long userId, @Param("increment") int increment);


    List<UserCommunityStatistic> selectListOrderByColumn(@Param("column") String underlineColumnName,
                                                         @Param("startIndex") int startIndex,
                                                         @Param("recordNum") Integer recordNum);
}

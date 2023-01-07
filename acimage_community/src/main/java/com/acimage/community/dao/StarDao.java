package com.acimage.community.dao;


import com.acimage.common.model.domain.Star;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StarDao extends BaseMapper<Star> {

    @Delete("delete from tb_star where user_id=#{userId} and topic_id=#{topicId}")
    Integer deleteByUserIdAndTopicId(@Param("userId") long userId, @Param("topicId") long topicId);

    @Delete("delete from tb_star where topic_id=#{topicId}")
    Integer deleteByTopicId(@Param("topicId") long topicId);

//    @Select("select * from tb_star where user_id=#{userId} and topic_id=#{topicId}")
//    Star selectOne(@Param("userId") long userId,@Param("topicId") long topicId);

    List<Star> selectStarsWithTopicOrderByCreateTime(@Param("userId") long userId, @Param("startIndex") int startIndex, @Param("recordNumber") int recordNumber);

    Integer countStarsOwnedBy(@Param("userId") long userId);

    @Select("select count(*) as star_count from tb_star where topic_id=#{topicId}")
    Integer countStarsByTopicId(@Param("topicId") long topicId);

    @Select("select count(*) as star_count from tb_star where user_id=#{userId}")
    Integer countStarsByUserId(@Param("userId") long userId);

}

package com.acimage.community.dao;

import cn.hutool.core.lang.Pair;
import com.acimage.common.model.domain.community.Topic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import reactor.util.annotation.Nullable;

import java.util.Date;
import java.util.List;

public interface TopicDao extends BaseMapper<Topic> {

    @Update("update tb_topic set title=#{title},content=#{content},update_time=now() where id=#{id} and deleted=0")
    Integer updateTopic(@Param("id") long id, @Param("title") String title, @Param("content") String content);

    Integer updatePvByIncrement(@Param("idAndIncrements") List<Pair<Long, Integer>> idAndIncrements);

    Integer batchUpdateColumnByIncrement(@Param("column") String underlineColumnName, @Param("idAndIncrements") List<Pair<Long, Integer>> idAndIncrements);

    @Update("update tb_topic set ${column}=${column}+#{increment} where id=#{id} and deleted=0")
    Integer updateColumnByIncrement(@Param("column") String column, @Param("id") long id, @Param("increment") int increment);

    Integer batchUpdateActivityTime(@Param("idAndActivityTimes") List<Pair<Long, Date>> idAndActivityTimes);

    List<Topic> selectTopicsWithUserImagesOrderByPageView(@Param("startTime") String startTime, @Nullable @Param("limit") Integer limit);

    List<Topic> selectTopicsWithUserOrderBy(@Param("column") String columnForOrder, @Param("limit") int limit);

    Topic selectTopicWithUser(@Param("id") long id);

    Topic selectTopicWithUserBasicAndTagIds(@Param("id") long id);

    List<Topic> selectTopicsWithUserImagesOrderByCreateTime(@Param("userId") long userId, @Param("startIndex") int startIndex, @Param("recordNumber") int recordNumber);

    List<Topic> selectTopicsWithUserBasicByIds(@Param("ids") List<Long> ids);

    @Select("select count(*) from tb_topic where user_id=#{userId} and deleted=0")
    Integer countTopics(@Param("userId") long userId);

}

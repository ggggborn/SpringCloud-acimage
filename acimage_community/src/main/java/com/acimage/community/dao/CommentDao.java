package com.acimage.community.dao;

import com.acimage.common.model.domain.community.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CommentDao extends BaseMapper<Comment> {

    @Delete("delete from tb_comment where topic_id=#{topicId}")
    Integer deleteByTopicId(@Param("topicId") long topicId);
    @Update("update tb_comment set content=#{content},update_time=now() where id=#{id}")
    Integer updateContentById(@Param("id") long id, @Param("content") String content);

    List<Comment> selectCommentsWithUserByTopicId(@Param("topicId") long topicId);

    @Select("select count(*) as comment_count from tb_comment where topic_id=#{topicId}")
    Integer countCommentsByTopicId(@Param("topicId") long topicId);

    @Select("select count(*) as count from tb_comment where user_id=#{userId}")
    Integer countCommentsByUserId(@Param("userId") long userId);

    List<Comment> selectCommentsWithUser(@Param("topicId") long topicId,@Param("startIndex") int startIndex,@Param("recordNumber") int recordNumber);

    List<Comment> selectCommentsWithTopicOrderByCreateTime(@Param("userId") long userId, @Param("startIndex") int startIndex, @Param("recordNumber") int recordNumber);
}

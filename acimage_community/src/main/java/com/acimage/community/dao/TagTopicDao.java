package com.acimage.community.dao;

import com.acimage.common.model.domain.community.TagTopic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TagTopicDao extends BaseMapper<TagTopic> {

    void insertBatch(List<TagTopic> tagTopicList);

    @Select("select tag_id from tb_tag_topic where topic_id=#{topicId} and deleted=0")
    List<Integer> selectTagIds(@Param("topicId") long topicId);
}

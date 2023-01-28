package com.acimage.community.dao;

import com.acimage.common.model.domain.community.TagTopic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface TagTopicDao extends BaseMapper<TagTopic> {

    void insertBatch(List<TagTopic> tagTopicList);
}

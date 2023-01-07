package com.acimage.community.service.topic;

import com.acimage.common.model.domain.Topic;

import java.util.List;

public interface TopicQueryService {
    Topic getTopic(long id);

    List<Topic> listTopics(List<Long> ids);


}

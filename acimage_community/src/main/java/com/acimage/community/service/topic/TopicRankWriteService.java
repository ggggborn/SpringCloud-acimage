package com.acimage.community.service.topic;

import com.acimage.common.model.domain.community.Topic;
import com.acimage.community.service.topic.enums.TopicAttribute;

import javax.validation.constraints.NotNull;

public interface TopicRankWriteService {

    void updateRank(TopicAttribute attr, long topicId, double newScore);

    void updateRank(TopicAttribute attr, @NotNull Topic topic);
}

package com.acimage.community.service.topic;

import com.acimage.common.model.domain.community.Topic;
import com.acimage.community.global.enums.TopicAttribute;

import java.util.Date;

public interface TopicSpAttrQueryService {
    Integer getPageView(long topicId);
    Date getActivityTime(long topicId);
    Integer getStarCount(long topicId);
    Integer getCommentCount(long topicId);
    void setAttrIntoTopic(Topic topic, TopicAttribute... attrs);

}

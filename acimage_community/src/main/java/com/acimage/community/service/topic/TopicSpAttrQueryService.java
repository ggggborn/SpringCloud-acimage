package com.acimage.community.service.topic;

import cn.hutool.core.lang.Pair;
import com.acimage.common.model.domain.Topic;
import com.acimage.community.service.topic.enums.TopicAttribute;

import java.util.Date;
import java.util.List;

public interface TopicSpAttrQueryService {
    Integer getPageView(long topicId);
    Date getActivityTime(long topicId);
    Integer getStarCount(long topicId);
    Integer getCommentCount(long topicId);
    void setAttrIntoTopic(Topic topic, TopicAttribute... attrs);

}

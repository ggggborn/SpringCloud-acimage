package com.acimage.community.service.topic;

import com.acimage.common.model.domain.TopicHtml;
import com.acimage.common.redis.annotation.QueryRedis;

public interface TopicHtmlQueryService {

    TopicHtml getTopicHtml(long topicId);
}

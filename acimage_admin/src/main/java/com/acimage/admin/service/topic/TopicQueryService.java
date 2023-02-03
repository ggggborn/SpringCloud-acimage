package com.acimage.admin.service.topic;

import com.acimage.admin.model.request.TopicQueryReq;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.page.Page;

public interface TopicQueryService {
    Page<Topic> listOrderByColumn(TopicQueryReq topicQueryReq);

    Integer getTopicCount();
}

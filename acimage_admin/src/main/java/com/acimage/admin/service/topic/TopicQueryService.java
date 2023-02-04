package com.acimage.admin.service.topic;

import com.acimage.admin.model.request.TopicQueryReq;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.page.MyPage;

public interface TopicQueryService {
    MyPage<Topic> listOrderByColumn(TopicQueryReq topicQueryReq);

    Integer getTopicCount();
}

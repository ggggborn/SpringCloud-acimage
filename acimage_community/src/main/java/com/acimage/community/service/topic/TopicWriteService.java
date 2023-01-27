package com.acimage.community.service.topic;

import com.acimage.common.model.domain.community.Topic;
import com.acimage.community.model.request.TopicModifyContentReq;

public interface TopicWriteService {
    void save(Topic topic);
    void remove(long id);
    void update(long id,String title,String content);

    void updateTitle(long id, String title);

    void updateContent(TopicModifyContentReq topicModifyContentReq);

    void updateContent(long id, String content);
}

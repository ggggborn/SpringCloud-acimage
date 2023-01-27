package com.acimage.community.service.topic;

import com.acimage.common.model.domain.community.TopicHtml;

public interface TopicHtmlWriteService {
    TopicHtml save(long topicId, String html);

    void remove(long topicId);

    void update(long topicId, String html);
}

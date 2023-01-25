package com.acimage.community.service.topic;

import com.acimage.common.model.domain.TopicHtml;

public interface TopicHtmlWriteService {
    TopicHtml save(long topicId, String html);

    void remove(long topicId);
}

package com.acimage.community.service.topic;

import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.page.Page;

public interface TopicSearchService {
    Page<Topic> searchByTagId(Integer tagId, int pageNo, int pageSize);
}

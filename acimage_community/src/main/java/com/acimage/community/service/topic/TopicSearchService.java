package com.acimage.community.service.topic;

import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.page.Page;

import java.util.List;

public interface TopicSearchService {
    Page<Topic> searchByTagId(Integer tagId, int pageNo, int pageSize);

    List<Topic> searchSimilar(long topicId, int size);

    List<Topic> searchSimilarByTitle(long topicId,String title, int size);
}

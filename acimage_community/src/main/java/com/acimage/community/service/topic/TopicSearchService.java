package com.acimage.community.service.topic;

import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.page.MyPage;
import com.acimage.community.model.request.SearchTopicReq;

import java.util.List;

public interface TopicSearchService {
    MyPage<Topic> searchByTagId(Integer tagId, int pageNo, int pageSize);

    List<Topic> searchSimilar(long topicId, int size);

    List<Topic> searchSimilarByTitle(long topicId,String title, int size);

    MyPage<Topic> search(SearchTopicReq searchTopicReq);
}

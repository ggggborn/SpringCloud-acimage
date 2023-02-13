package com.acimage.community.service.topic;

import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.page.MyPage;
import com.acimage.community.model.request.TopicQueryByCategoryIdReq;
import com.acimage.community.model.request.TopicQueryBySortReq;
import com.acimage.community.model.request.TopicQueryByTagIdReq;
import com.acimage.community.model.request.TopicSearchReq;

import java.util.List;

public interface TopicEsSearchService {
    List<Topic> searchSimilar(long topicId, int size);

    List<Topic> searchSimilarByTitle(long topicId,String title, int size);
    MyPage<Topic> searchByTagId(TopicQueryByTagIdReq queryReq);
    MyPage<Topic> searchBySort(TopicQueryByCategoryIdReq queryReq);

    MyPage<Topic> searchBySort(TopicQueryBySortReq queryReq);

    MyPage<Topic> search(TopicSearchReq topicSearchReq);
}

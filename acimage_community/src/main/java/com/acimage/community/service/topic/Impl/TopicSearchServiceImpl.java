package com.acimage.community.service.topic.Impl;

import com.acimage.common.model.Index.TopicIndex;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.page.Page;
import com.acimage.common.utils.EsUtils;
import com.acimage.common.utils.LambdaUtils;
import com.acimage.community.service.topic.TopicSearchService;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicSearchServiceImpl implements TopicSearchService {
    @Autowired
    EsUtils esUtils;

    @Override
    public Page<Topic> searchByTagId(Integer tagId, int pageNo, int pageSize){
        String column= LambdaUtils.columnNameOf(Topic::getTagIds);
        Page<TopicIndex> topicIndexPage = esUtils.termQuery(column, tagId, TopicIndex.class, pageNo, pageSize);
        List<Topic> topicList=TopicIndex.toTopicList(topicIndexPage.getDataList());
        return new Page<>(topicIndexPage.getTotalCount(),topicList);
    }

    @Override
    public List<Topic> searchSimilar(long topicId, int size){
        List<String> columns=LambdaUtils.columnsFrom(Topic::getContent,Topic::getTitle);
        List<TopicIndex> topicIndices = esUtils.similarQuery(Long.toString(topicId), TopicIndex.class, columns, 1, size);
        return TopicIndex.toTopicList(topicIndices);
    }

    @Override
    public List<Topic> searchSimilarByTitle(long topicId,String title, int size){
        String column=LambdaUtils.columnNameOf(TopicIndex::getTitle);
        float score=2;
        List<TopicIndex> topicIndices = esUtils.matchQuery( TopicIndex.class, column, title,1, size+1,score);
        return TopicIndex.toTopicList(topicIndices).stream().filter(o->{return !o.getId().equals(topicId);}).collect(Collectors.toList());
    }
}

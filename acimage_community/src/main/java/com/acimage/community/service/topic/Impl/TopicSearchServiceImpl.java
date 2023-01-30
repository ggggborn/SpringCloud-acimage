package com.acimage.community.service.topic.Impl;

import com.acimage.common.model.Index.TopicIndex;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.page.Page;
import com.acimage.common.utils.EsUtils;
import com.acimage.common.utils.LambdaUtils;
import com.acimage.community.service.topic.TopicSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}

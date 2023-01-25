package com.acimage.community.service.topic.Impl;

import com.acimage.common.model.domain.TopicHtml;
import com.acimage.common.redis.annotation.QueryRedis;
import com.acimage.community.dao.TopicHtmlDao;
import com.acimage.community.service.topic.TopicHtmlQueryService;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicHtmlQueryServiceImpl implements TopicHtmlQueryService {
    @Autowired
    TopicHtmlDao topicHtmlDao;
    @Autowired
    SensitiveWordBs sensitiveWordBs;

    @Override
    @QueryRedis(keyPrefix = "acimage:community:topicHtml:topicId:",expire = 12L)
    public TopicHtml getTopicHtml(long topicId){
        TopicHtml topicHtml=topicHtmlDao.selectById(topicId);
        topicHtml.setHtml(sensitiveWordBs.replace(topicHtml.getHtml()));
        return topicHtml;
    }
}

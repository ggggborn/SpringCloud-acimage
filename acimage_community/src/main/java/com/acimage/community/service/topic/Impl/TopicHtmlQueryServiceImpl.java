package com.acimage.community.service.topic.Impl;

import com.acimage.common.model.domain.community.TopicHtml;
import com.acimage.common.redis.annotation.QueryRedis;
import com.acimage.common.utils.SensitiveWordUtils;
import com.acimage.community.dao.TopicHtmlDao;
import com.acimage.community.service.topic.TopicHtmlQueryService;
import com.acimage.community.global.consts.TopicKeyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicHtmlQueryServiceImpl implements TopicHtmlQueryService {
    @Autowired
    TopicHtmlDao topicHtmlDao;

    @Override
    @QueryRedis(keyPrefix = TopicKeyConstants.HASHKP_TOPIC_HTML, expire = 12L)
    public TopicHtml getTopicHtml(long topicId) {
        TopicHtml topicHtml = topicHtmlDao.selectById(topicId);
        topicHtml.setHtml(SensitiveWordUtils.filter(topicHtml.getHtml()));
        return topicHtml;
    }
}

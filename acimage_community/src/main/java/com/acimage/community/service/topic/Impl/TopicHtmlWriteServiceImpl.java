package com.acimage.community.service.topic.Impl;

import com.acimage.common.model.domain.TopicHtml;
import com.acimage.community.dao.TopicHtmlDao;
import com.acimage.community.service.topic.TopicHtmlWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicHtmlWriteServiceImpl implements TopicHtmlWriteService {
    @Autowired
    TopicHtmlDao topicHtmlDao;

    @Override
    public TopicHtml save(long topicId, String html) {
        TopicHtml topicHtml = new TopicHtml();
        topicHtml.setTopicId(topicId);
        topicHtml.setHtml(html);
        topicHtmlDao.insert(topicHtml);
        return topicHtml;
    }

    @Override
    public void remove(long topicId) {
        topicHtmlDao.deleteById(topicId);
    }


    public void update() {

    }
}

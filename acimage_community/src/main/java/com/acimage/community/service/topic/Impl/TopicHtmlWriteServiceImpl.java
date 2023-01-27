package com.acimage.community.service.topic.Impl;

import com.acimage.common.model.domain.TopicHtml;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.community.dao.TopicHtmlDao;
import com.acimage.community.service.topic.TopicHtmlWriteService;
import com.acimage.community.service.topic.consts.KeyConstants;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicHtmlWriteServiceImpl implements TopicHtmlWriteService {
    @Autowired
    TopicHtmlDao topicHtmlDao;
    @Autowired
    RedisUtils redisUtils;

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
        redisUtils.delete(KeyConstants.HASHKP_TOPIC_HTML+topicId);
    }


    @Override
    public void update(long topicId, String html) {
        LambdaUpdateWrapper<TopicHtml> uw=new LambdaUpdateWrapper<>();
        uw.eq(TopicHtml::getTopicId,topicId)
                .set(TopicHtml::getHtml,html);
        topicHtmlDao.update(null,uw);
        redisUtils.delete(KeyConstants.HASHKP_TOPIC_HTML+topicId);
     }
}

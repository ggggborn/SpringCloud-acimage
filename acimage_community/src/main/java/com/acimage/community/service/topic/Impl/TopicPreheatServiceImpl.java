package com.acimage.community.service.topic.Impl;

import com.acimage.common.model.domain.Topic;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.community.dao.TopicDao;
import com.acimage.community.service.topic.TopicPreheatService;
import com.acimage.community.service.topic.TopicRankWriteService;
import com.acimage.community.service.topic.consts.KeyConstants;
import com.acimage.community.service.topic.enums.TopicAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TopicPreheatServiceImpl implements TopicPreheatService {

    @Autowired
    TopicDao topicDao;
    @Autowired
    TopicRankWriteService topicRankWriteService;
    @Autowired
    RedisUtils redisUtils;

    @Override
    public void preheatTopicsOrderBy(TopicAttribute attr, int sizeOfLoadIntoRank, int numberOfCacheTopics, long timeout, TimeUnit timeUnit) {
        String underlineColumnName = attr.toUnderlineColumnName();

        List<Topic> hotTopics = topicDao.selectTopicsWithUserOrderBy(underlineColumnName, sizeOfLoadIntoRank);

        for (int i = 0; i < hotTopics.size(); i++) {
            Topic topic = hotTopics.get(i);
            topicRankWriteService.updateRank(attr, topic);

            if (i < numberOfCacheTopics) {
                String key = KeyConstants.HASHKP_TOPIC + topic.getId();
                redisUtils.setObjectForHash(key, topic, timeout, timeUnit);
            }
        }
    }
}

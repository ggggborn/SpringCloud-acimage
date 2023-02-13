package com.acimage.community.service.topic.Impl;

import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.utils.LambdaUtils;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.community.dao.TopicDao;
import com.acimage.community.service.topic.TopicPreheatService;
import com.acimage.community.service.topic.TopicRankWriteService;
import com.acimage.community.global.consts.TopicKeyConstants;
import com.acimage.community.global.enums.TopicAttribute;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
    public void preheatTopicsOrderBy(TopicAttribute attr, int rankSize, int cacheSize, long timeout, TimeUnit timeUnit) {
        String underlineColumnName = attr.toUnderlineColumnName();
        LambdaQueryWrapper<Topic> qw = new LambdaQueryWrapper<>();
        qw.last(String.format(" order by %s desc limit %d", underlineColumnName, rankSize));

        List<Topic> hotTopics = topicDao.selectList(qw);
//        List<Topic> hotTopics = topicDao.selectTopicsWithUserOrderBy(underlineColumnName, rankSize);


        for (int i = 0; i < hotTopics.size(); i++) {
            Topic topic = hotTopics.get(i);
            topicRankWriteService.updateRank(attr, topic);
//
//            if (i < cacheSize) {
//                String key = TopicKeyConstants.HASHKP_TOPIC + topic.getId();
//                redisUtils.setObjectForHash(key, topic, timeout, timeUnit);
//            }
        }
    }
}

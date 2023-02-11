package com.acimage.community.service.topic.Impl;

import com.acimage.common.global.context.UserContext;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.redis.annotation.QueryRedis;
import com.acimage.common.redis.enums.DataType;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.community.dao.TopicDao;
import com.acimage.community.service.topic.TopicQueryService;
import com.acimage.community.global.consts.TopicKeyConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TopicQueryServiceImpl implements TopicQueryService {
    @Autowired
    TopicDao topicDao;
    @Autowired
    RedisUtils redisUtils;

    @QueryRedis(expire = 11L, keyPrefix = TopicKeyConstants.HASHKP_TOPIC, dataType = DataType.HASH)
    @Override
    public Topic getTopic(long id) {
        Topic topic = topicDao.selectTopicWithUser(id);
        if (topic == null) {
            log.error("user:{} 查询 对象：话题{} error：话题不存在", UserContext.getUsername(), id);
            return null;
        }
        return topic;
    }

    @Override
    public List<Topic> listTopicWithUser(List<Long> ids) {
        return topicDao.selectTopicsWithUserByIds(ids);
    }

    @Override
    public List<Topic> listByIds(List<Long> ids) {
        return topicDao.selectBatchIds(ids);
    }
}

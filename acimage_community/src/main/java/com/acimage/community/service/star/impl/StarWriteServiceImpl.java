package com.acimage.community.service.star.impl;


import com.acimage.common.global.context.UserContext;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.exception.BusinessException;
import com.acimage.common.model.domain.community.Star;
import com.acimage.community.service.cmtyuser.CmtyUserWriteService;
import com.acimage.community.global.consts.StarKeyConsts;
import com.acimage.community.service.topic.TopicQueryService;
import com.acimage.community.service.star.StarWriteService;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.community.dao.StarDao;
import com.acimage.community.service.topic.TopicSpAttrWriteService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class StarWriteServiceImpl implements StarWriteService {

    @Autowired
    StarDao starDao;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    TopicQueryService topicQueryService;
    @Autowired
    TopicSpAttrWriteService topicSpAttrWriteService;
    @Autowired
    CmtyUserWriteService cmtyUserWriteService;

    @Override
    public void saveStar(long userId, long topicId) {
        Star star = new Star(userId, topicId);

        try {
            starDao.insert(star);
        } catch (DuplicateKeyException e) {
            log.warn("user:{} 收藏过了话题 topicId:{}", UserContext.getUsername(), topicId);
            throw new BusinessException("已经收藏过了，请刷新重试");
        }

        redisUtils.setObjectJson(StarKeyConsts.keyOfIsStar(userId, topicId), Boolean.TRUE, 30, TimeUnit.SECONDS);

        int increment=1;
        topicSpAttrWriteService.increaseStarCount(topicId, increment);
        Topic topic = topicQueryService.getTopic(topicId);
        if (topic != null) {
            cmtyUserWriteService.updateStarCountByIncrement(topic.getUserId(),increment);
        }
    }

    @Override
    public void removeStar(long userId, long topicId) {
        int col = starDao.deleteByUserIdAndTopicId(userId, topicId);
        //删除缓存
        redisUtils.delete(StarKeyConsts.keyOfIsStar(userId, topicId));

        //更新话题、话题主人收藏量
        int starIncrement = -col;
        topicSpAttrWriteService.increaseStarCount(topicId, starIncrement);
        Topic topic = topicQueryService.getTopic(topicId);
        if (topic != null) {
            long ownerId = topic.getUserId();
            cmtyUserWriteService.updateStarCountByIncrement(ownerId, starIncrement);
        }
    }


    @Override
    public void removeStars(long topicId) {
        LambdaQueryWrapper<Star> qw = new LambdaQueryWrapper<>();
        qw.eq(Star::getTopicId, topicId);
        int starIncrement = -starDao.delete(qw);

        //更新话题主人收藏量
        Topic topic = topicQueryService.getTopic(topicId);
        if (topic != null) {
            long ownerId = topic.getUserId();
            cmtyUserWriteService.updateStarCountByIncrement(ownerId, starIncrement);
        }
    }


}

package com.acimage.community.service.star.impl;


import com.acimage.common.global.context.UserContext;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.global.exception.BusinessException;
import com.acimage.common.model.domain.community.Star;
import com.acimage.community.service.cmtyuser.CmtyUserWriteService;
import com.acimage.community.global.consts.StarKeyConstants;
import com.acimage.community.service.star.StarQueryService;
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

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class StarWriteServiceImpl implements StarWriteService {

    @Autowired
    StarDao starDao;
    @Autowired
    StarQueryService starQueryService;
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
        if (starQueryService.isStar(userId, topicId)) {
            log.info("user:{}已经点赞过话题topicId:{}", UserContext.getUsername(), topicId);
            throw new BusinessException("已经点过赞了");
        }

        Star star = new Star(userId, topicId);
        star.setUserId(userId);
        star.setTopicId(topicId);
        star.setCreateTime(new Date());

        try {
            starDao.insert(star);
        } catch (DuplicateKeyException e) {
            log.warn("user:{} 收藏过了话题 topicId:{}", UserContext.getUsername(), topicId);
            throw new BusinessException("已经收藏过了，请刷新重试");
        }

        int increment = 1;
        topicSpAttrWriteService.increaseStarCount(topicId, increment);
        //更新主人star
        Topic topic = topicQueryService.getTopic(topicId);
        if (topic != null) {
            cmtyUserWriteService.updateStarCountByIncrement(topic.getUserId(), increment);
        }

        long timeout = 3L;
        redisUtils.setObjectJson(StarKeyConstants.keyOfIsStar(userId, topicId), Boolean.TRUE, timeout, TimeUnit.SECONDS);
    }

    @Override
    public void removeStar(long userId, long topicId) {
        if (!starQueryService.isStar(userId, topicId)) {
            log.info("user:{}已经取消点赞过话题topicId:{}", UserContext.getUsername(), topicId);
            throw new BusinessException("已经取消过点赞了");
        }
        int col = starDao.deleteByUserIdAndTopicId(userId, topicId);
        //删除缓存
        redisUtils.delete(StarKeyConstants.keyOfIsStar(userId, topicId));

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

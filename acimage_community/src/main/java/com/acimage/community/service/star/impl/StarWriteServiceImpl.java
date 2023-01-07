package com.acimage.community.service.star.impl;


import com.acimage.common.model.domain.Topic;
import com.acimage.common.exception.BusinessException;
import com.acimage.common.model.domain.Star;
import com.acimage.community.service.star.consts.KeyConsts;
import com.acimage.community.service.topic.TopicQueryService;
import com.acimage.community.service.star.StarWriteService;
import com.acimage.common.utils.RedisUtils;
import com.acimage.community.dao.StarDao;
import com.acimage.community.service.topic.TopicSpAttrWriteService;
import com.acimage.community.service.userstatistic.UserCsWriteService;
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
    UserCsWriteService userCsWriteService;

    @Override
    public void saveStar(long userId, long topicId) {
        Star star = new Star(userId, topicId);

        try {
            starDao.insert(star);
        } catch (DuplicateKeyException e) {
            log.error("用户{} 收藏过了话题 {}", userId, topicId);
            throw new BusinessException("已经收藏过了，请刷新重试");
        }

        //更新话题、话题主人收藏量
        int starIncrement = 1;
        topicSpAttrWriteService.increaseStarCount(topicId, starIncrement);

        Topic topic = topicQueryService.getTopic(topicId);
        if (topic != null) {
            long ownerId = topic.getUserId();
            userCsWriteService.updateStarCountByIncrement(ownerId, starIncrement);
        }

        redisUtils.setObjectJson(KeyConsts.keyOfIsStar(userId, topicId), Boolean.TRUE, 3L, TimeUnit.MINUTES);
    }

    @Override
    public void removeStar(long userId, long topicId) {
        int col = starDao.deleteByUserIdAndTopicId(userId, topicId);
        //删除缓存
        redisUtils.delete(KeyConsts.keyOfIsStar(userId, topicId));

        //更新话题、话题主人收藏量
        int starIncrement = -col;
        topicSpAttrWriteService.increaseStarCount(topicId, starIncrement);
        Topic topic = topicQueryService.getTopic(topicId);
        if (topic != null) {
            long ownerId = topic.getUserId();
            userCsWriteService.updateStarCountByIncrement(ownerId, starIncrement);
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
            userCsWriteService.updateStarCountByIncrement(ownerId, starIncrement);
        }
    }


}

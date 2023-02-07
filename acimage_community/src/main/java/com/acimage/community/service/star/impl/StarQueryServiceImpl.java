package com.acimage.community.service.star.impl;

import com.acimage.common.model.domain.community.Star;
import com.acimage.common.redis.annotation.KeyParam;
import com.acimage.common.redis.annotation.QueryRedis;
import com.acimage.community.dao.StarDao;
import com.acimage.community.service.star.StarQueryService;
import com.acimage.community.global.consts.StarKeyConsts;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StarQueryServiceImpl implements StarQueryService {
    @Autowired
    StarDao starDao;

    @QueryRedis(keyPrefix = StarKeyConsts.STRINGKP_STAR_USER_TOPIC)
    @Override
    public boolean isStar(@KeyParam long userId,@KeyParam long topicId) {
        LambdaQueryWrapper<Star> qw=new LambdaQueryWrapper<>();
        qw.eq(Star::getUserId,userId)
                .eq(Star::getTopicId,topicId);
        return starDao.selectOne(qw) != null;
    }

    @QueryRedis(keyPrefix = StarKeyConsts.STRINGKP_TOPIC_STAR_COUNT,expire = 31L)
    @Override
    public Integer getTopicStarCount(@KeyParam long topicId) {
        LambdaQueryWrapper<Star> qw=new LambdaQueryWrapper<>();
        qw.eq(Star::getTopicId,topicId);
        return starDao.selectCount(qw);
    }

    @QueryRedis(keyPrefix = StarKeyConsts.STRINGKP_USER_STAR_COUNT, expire = 31L)
    @Override
    public Integer getStarCountOwnedBy(@KeyParam long userId) {
        return starDao.countStarsOwnedBy(userId);
    }
}

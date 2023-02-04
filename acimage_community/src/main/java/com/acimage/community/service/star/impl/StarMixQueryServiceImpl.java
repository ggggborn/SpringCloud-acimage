package com.acimage.community.service.star.impl;

import com.acimage.common.model.domain.community.Star;
import com.acimage.common.redis.annotation.QueryRedis;
import com.acimage.community.dao.StarDao;
import com.acimage.community.global.consts.PageSizeConstants;
import com.acimage.common.model.page.MyPage;
import com.acimage.community.service.star.StarMixQueryService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class StarMixQueryServiceImpl implements StarMixQueryService {
    @Autowired
    StarDao starDao;
    @QueryRedis(keyPrefix = "acimage:stars:userId:pageNo:", expire = 30L, unit = TimeUnit.SECONDS)
    @Override
    public MyPage<Star> pageStarsWithTopic(long userId, int pageNo) {
        int startIndex = (pageNo - 1) * PageSizeConstants.ACTIVITY_STARS;
        List<Star> stars = starDao.selectStarsWithTopicOrderByCreateTime(userId, startIndex, PageSizeConstants.ACTIVITY_STARS);

        LambdaUpdateWrapper<Star> qw=new LambdaUpdateWrapper<>();
        qw.eq(Star::getUserId,userId);
        int totalCount = starDao.selectCount(qw);

        return new MyPage<>(totalCount, stars);
    }
}

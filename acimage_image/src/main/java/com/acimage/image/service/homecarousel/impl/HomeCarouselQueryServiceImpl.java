package com.acimage.image.service.homecarousel.impl;

import com.acimage.common.model.domain.image.HomeCarousel;
import com.acimage.common.redis.annotation.QueryRedis;

import com.acimage.image.dao.HomeCarrouselDao;
import com.acimage.image.service.homecarousel.HomeCarouselQueryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class HomeCarouselQueryServiceImpl implements HomeCarouselQueryService {
    public static final String STRINGK_SP_IMAGE = "acimage:image:homeCarousel";
    @Autowired
    HomeCarrouselDao homeCarrouselDao;

    @QueryRedis(keyPrefix = STRINGK_SP_IMAGE, expire = 24, unit = TimeUnit.HOURS)
    @Override
    public List<HomeCarousel> listAll() {
        LambdaQueryWrapper<HomeCarousel> qw = new LambdaQueryWrapper<>();
        qw.orderByAsc(HomeCarousel::getLocation);
        return homeCarrouselDao.selectList(qw);
    }
}

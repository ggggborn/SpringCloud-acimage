package com.acimage.image.service.spimage.impl;

import com.acimage.common.global.enums.ImageType;
import com.acimage.common.model.domain.SpImage;
import com.acimage.common.redis.annotation.QueryRedis;

import com.acimage.image.dao.SpImageDao;
import com.acimage.image.service.spimage.SpImageQueryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class SpImageQueryServiceImpl implements SpImageQueryService {
    public static final String STRINGK_SP_IMAGE = "acimage:image:spImages:homeCarousel";
    @Autowired
    SpImageDao spImageDao;

    @QueryRedis(keyPrefix = STRINGK_SP_IMAGE, expire = 24, unit = TimeUnit.HOURS)
    @Override
    public List<SpImage> queryHomeCarousel() {
        LambdaQueryWrapper<SpImage> qw = new LambdaQueryWrapper<>();
        qw.eq(SpImage::getType, ImageType.HOME_CAROUSEL)
                .orderByAsc(SpImage::getLocation);
        return spImageDao.selectList(qw);
    }
}

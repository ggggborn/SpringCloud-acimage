package com.acimage.admin.service.homecarousel.impl;

import com.acimage.admin.dao.image.HomeCarouselDao;
import com.acimage.admin.service.homecarousel.HomeCarouselQueryService;
import com.acimage.common.model.domain.image.HomeCarousel;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("image")
public class HomeCarouselQueryServiceImpl implements HomeCarouselQueryService {

    @Autowired
    HomeCarouselDao homeCarouselDao;
    @Override
    public List<HomeCarousel> listCurrent() {
        LambdaQueryWrapper<HomeCarousel> qw = new LambdaQueryWrapper<>();
        qw.orderByAsc(HomeCarousel::getLocation);
        return homeCarouselDao.selectList(qw);
    }
}

package com.acimage.admin.service.homecarousel;

import com.acimage.common.model.domain.community.HomeCarousel;

import java.util.List;

public interface HomeCarouselQueryService {
    List<HomeCarousel> listCurrent();
}

package com.acimage.admin.service.homecarousel;

import com.acimage.common.model.domain.image.HomeCarousel;

import java.util.List;

public interface HomeCarouselQueryService {
    List<HomeCarousel> listCurrent();
}

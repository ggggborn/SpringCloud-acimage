package com.acimage.community.web.controller;


import com.acimage.common.deprecated.annotation.Authentication;
import com.acimage.common.global.enums.AuthenticationType;
import com.acimage.common.model.domain.community.HomeCarousel;
import com.acimage.common.result.Result;

import com.acimage.community.service.homecarousel.HomeCarouselQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Validated
@Authentication(type = AuthenticationType.NONE)
@RequestMapping("/api/community/homeCarousels")
public class HomeCarouselQueryController {
    @Autowired
    HomeCarouselQueryService homeCarouselQueryService;

    @GetMapping("/list")
    public Result<List<HomeCarousel>> queryHomeCarousel() {
        List<HomeCarousel> homeCarousels = homeCarouselQueryService.listAll();
        return Result.ok(homeCarousels);
    }


}

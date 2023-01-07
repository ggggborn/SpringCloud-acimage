package com.acimage.image.web.controller;


import com.acimage.common.global.annotation.Authentication;
import com.acimage.common.global.enums.AuthenticationType;
import com.acimage.common.model.domain.SpImage;
import com.acimage.common.result.Result;

import com.acimage.image.service.spimage.SpImageQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Validated
@Authentication(type = AuthenticationType.NONE)
@RequestMapping("/api/image/spImages")
public class SpImageQueryController {
    @Autowired
    SpImageQueryService spImageQueryService;

    @GetMapping("/homeCarousel")
    public Result<List<SpImage>> queryHomeCarousel() {
        List<SpImage> spImages = spImageQueryService.queryHomeCarousel();
        return Result.ok(spImages);
    }


}

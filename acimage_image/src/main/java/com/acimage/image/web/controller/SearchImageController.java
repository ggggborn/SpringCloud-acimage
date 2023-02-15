package com.acimage.image.web.controller;


import com.acimage.common.deprecated.annotation.Authentication;
import com.acimage.common.model.domain.image.Image;
import com.acimage.common.redis.annotation.RequestLimit;
import com.acimage.common.redis.enums.LimitTarget;
import com.acimage.common.result.Result;
import com.acimage.image.service.imagehash.SearchImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@Validated
@Authentication
@RequestMapping("/api/image/images")
public class SearchImageController {
    @Autowired
    SearchImageService searchImageService;


    @RequestLimit(limitTimes = {1,10}, durations = {3,1}, penaltyTimes = {-1,-1}, targets = {LimitTarget.IP,LimitTarget.ALL})
    @PostMapping("/searchByImage")
    public Result<List<Image>> searchImageWithTopicByImage(@RequestParam("imageFile") MultipartFile multipartFile) {
        return Result.ok(searchImageService.searchMostSimilarImages(multipartFile));
    }
}

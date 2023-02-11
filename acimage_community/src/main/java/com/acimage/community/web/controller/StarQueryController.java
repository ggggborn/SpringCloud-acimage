package com.acimage.community.web.controller;


import com.acimage.common.deprecated.annotation.Authentication;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.result.Result;
import com.acimage.community.service.star.StarMixQueryService;
import com.acimage.community.service.star.StarQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@RestController
@Slf4j
@Validated
@Authentication
@RequestMapping("/api/community/stars/query")
public class StarQueryController {
    @Autowired
    StarQueryService starQueryService;
    @Autowired
    StarMixQueryService starMixQueryService;

    @GetMapping("/mine/{pageNo}")
    public Result<?> pageMyStars(@Positive @PathVariable("pageNo") Integer pageNo) {
        return Result.ok(starMixQueryService.pageStarsWithTopic(UserContext.getUserId(),pageNo));
    }

    @GetMapping("/isStar/{topicId}")
    public Result<?> isStar(@Positive @PathVariable("topicId") Long topicId){
        return Result.ok(starQueryService.isStar(UserContext.getUserId(),topicId));
    }
}

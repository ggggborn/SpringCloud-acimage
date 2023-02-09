package com.acimage.community.web.controller;



import com.acimage.common.result.Result;
import com.acimage.common.deprecated.annotation.Authentication;
import com.acimage.common.global.context.UserContext;
import com.acimage.community.service.star.StarMixQueryService;
import com.acimage.community.service.star.StarQueryService;
import com.acimage.community.service.star.StarWriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@RestController
@Slf4j
@Validated
@Authentication
@RequestMapping("/api/community/stars/operate")
public class StarOperateController {
    @Autowired
    StarWriteService starWriteService;
    @Autowired
    StarQueryService starQueryService;
    @Autowired
    StarMixQueryService starMixQueryService;

    @PostMapping("/{topicId}")
    public Result addStar(@Positive @PathVariable("topicId") Long topicId) {
        starWriteService.saveStar(UserContext.getUserId(),topicId);
        return Result.ok();
    }

    @DeleteMapping("/{topicId}")
    public Result deleteStar(@Positive @PathVariable("topicId") Long topicId) {
        starWriteService.removeStar(UserContext.getUserId(),topicId);
        return Result.ok();
    }

}

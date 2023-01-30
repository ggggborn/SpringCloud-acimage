package com.acimage.community.web.controller;


import com.acimage.common.model.domain.community.CmtyUser;
import com.acimage.common.result.Result;
import com.acimage.community.service.cmtyuser.CmtyUserRankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/community/users")
@Validated
public class UserRankController {

    @Autowired
    CmtyUserRankService cmtyUserRankService;

    @GetMapping("/rank/topicCount/{pageNo}")
    public Result<List<CmtyUser>> pageUsersRankByTopicCount(@PathVariable @Max(10) Integer pageNo) {
        int pageSize=10;
        return Result.ok(cmtyUserRankService.pageUserRankByTopicCount(pageNo, pageSize));
    }

    @GetMapping("/rank/starCount/{pageNo}")
    public Result<List<CmtyUser>> pageUsersRankByStarCount(@PathVariable @Max(10) Integer pageNo) {
        int pageSize=10;
        return Result.ok(cmtyUserRankService.pageUserRankByStarCount(pageNo, pageSize));
    }
}

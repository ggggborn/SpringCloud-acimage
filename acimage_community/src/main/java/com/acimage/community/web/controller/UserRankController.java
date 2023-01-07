package com.acimage.community.web.controller;


import com.acimage.common.global.annotation.Authentication;
import com.acimage.common.model.domain.User;
import com.acimage.common.result.Result;
import com.acimage.community.service.userstatistic.UserCsRankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/community/users")
@Validated
public class UserRankController {

    @Autowired
    UserCsRankService userCsRankService;

    @GetMapping("/rank/topicCount/{pageNo}")
    public Result<List<User>> pageUsersRankByTopicCount(@PathVariable @Max(10) Integer pageNo) {
        int pageSize=10;
        return Result.ok(userCsRankService.pageUserRankByTopicCount(pageNo, pageSize));
    }

    @GetMapping("/rank/starCount/{pageNo}")
    public Result<List<User>> pageUsersRankByStarCount(@PathVariable @Max(10) Integer pageNo) {
        int pageSize=10;
        return Result.ok(userCsRankService.pageUserRankByStarCount(pageNo, pageSize));
    }
}

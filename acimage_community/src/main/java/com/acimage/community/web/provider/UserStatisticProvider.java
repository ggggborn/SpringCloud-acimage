package com.acimage.community.web.provider;


import com.acimage.common.global.annotation.Authentication;
import com.acimage.common.model.domain.UserCommunityStatistic;
import com.acimage.common.result.Result;
import com.acimage.community.service.userstatistic.UserCsQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/community/userStatistics")
@Validated
@Authentication
public class UserStatisticProvider {
    @Autowired
    UserCsQueryService userCsQueryService;

    @GetMapping("/userId/{userId}")
    public Result<UserCommunityStatistic> queryUserCommunityStatistic(@PathVariable Long userId) {
        return Result.ok(userCsQueryService.getUserCommunityStatistic(userId));
    }
}

package com.acimage.user.web.controller;


import com.acimage.common.deprecated.annotation.Authentication;
import com.acimage.common.redis.annotation.RequestLimit;
import com.acimage.common.redis.enums.LimitTarget;
import com.acimage.common.result.Result;
import com.acimage.user.model.vo.ProfileVo;
import com.acimage.user.service.user.UserInfoService;
import com.acimage.user.service.user.UserQueryService;
import com.acimage.user.service.user.UserWriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Size;

@Slf4j
@RestController
@RequestMapping("/api/user/users/query")
@Validated
@Authentication
public class UserQueryController {

    @Autowired
    UserInfoService userInfoService;

    @RequestLimit(limitTimes = {15}, durations = {5}, penaltyTimes = {-1}, targets = {LimitTarget.IP})
    @GetMapping("/me")
    public Result<ProfileVo> me() {
        return Result.ok(userInfoService.getProfile());
    }


}

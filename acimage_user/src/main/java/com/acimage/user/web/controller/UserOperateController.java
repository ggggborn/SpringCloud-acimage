package com.acimage.user.web.controller;


import com.acimage.common.deprecated.annotation.Authentication;
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
@RequestMapping("/api/user/users/operate")
@Validated
@Authentication
public class UserOperateController {
    @Autowired
    UserWriteService userWriteService;

    @PutMapping("/username/{username}")
    public Result<?> modifyUsername(@Size(min = 2, max = 12, message = "用户名长度在2到12之间") @PathVariable String username, HttpServletResponse resp) {
        String newToken = userWriteService.updateUsername(username, resp);
        return Result.ok(newToken);
    }

}

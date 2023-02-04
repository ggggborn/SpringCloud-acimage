package com.acimage.admin.web.controller;


import com.acimage.admin.model.request.UserQueryReq;
import com.acimage.admin.service.userrole.UserRoleWriteService;
import com.acimage.common.model.domain.user.User;
import com.acimage.common.model.page.MyPage;
import com.acimage.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@Slf4j
@RequestMapping("/api/admin/userRoles/operate")
@Validated
public class UserRoleOperateController {
    @Autowired
    UserRoleWriteService userRoleWriteService;

    @PostMapping
    public Result<?> addRoleForUser(@Positive @NotNull Long userId, @Positive @NotNull Integer roleId) {
        userRoleWriteService.save(userId, roleId);
        return Result.ok();
    }

    @DeleteMapping("/{userId}/{roleId}")
    public Result<?> deleteRoleForUser(@PathVariable @Positive @NotNull Long userId,@PathVariable @Positive @NotNull Integer roleId) {
        userRoleWriteService.remove(userId, roleId);
        return Result.ok();
    }
}

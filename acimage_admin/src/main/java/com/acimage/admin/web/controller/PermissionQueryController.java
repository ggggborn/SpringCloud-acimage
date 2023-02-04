package com.acimage.admin.web.controller;


import com.acimage.admin.service.permission.PermissionQueryService;
import com.acimage.common.model.domain.sys.Permission;
import com.acimage.common.model.page.MyPage;
import com.acimage.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequestMapping("/api/admin/permissions")
public class PermissionQueryController {
    @Autowired
    PermissionQueryService permissionQueryService;

    @GetMapping("/tree")
    public Result<List<Permission>> queryPermissionTree(){
        return Result.ok(permissionQueryService.getPermissionTree());
    }

    @GetMapping("/page/{pageNo}/{pageSize}")
    public Result<MyPage<Permission>> pagePermissionsWithParent(@PathVariable @Positive Integer pageNo,
                                                                @PathVariable @Max(20) Integer pageSize){
        return Result.ok(permissionQueryService.pagePermissionsWithParent(pageNo,pageSize));
    }

    @GetMapping("/modules")
    public Result<List<Permission>> queryModules(){
        return Result.ok(permissionQueryService.listModules());
    }
}

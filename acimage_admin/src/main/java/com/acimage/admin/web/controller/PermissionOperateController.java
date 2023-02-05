package com.acimage.admin.web.controller;

import cn.hutool.core.util.StrUtil;
import com.acimage.admin.model.request.PermissionAddReq;
import com.acimage.admin.model.request.PermissionModifyReq;
import com.acimage.admin.service.permission.PermissionWriteSercice;
import com.acimage.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@RestController
@Slf4j
@Validated
@RequestMapping("/api/admin/permissions")
public class PermissionOperateController {
    @Autowired
    PermissionWriteSercice permissionWriteSercice;

    @PostMapping
    public Result<?> add(@Validated @RequestBody PermissionAddReq permissionAddReq){

        if(permissionAddReq.getCode()==null|| StrUtil.isBlank(permissionAddReq.getCode())){
            permissionAddReq.setCode(null);
        }
        if(!permissionAddReq.getModule() &&permissionAddReq.getCode()==null){
            return Result.fail("非模块的权限码不能为空");
        }
        permissionWriteSercice.save(permissionAddReq);
        return Result.ok();
    }

    @PutMapping
    public Result<?> modify(@Validated @RequestBody PermissionModifyReq permissionModifyReq){
        permissionWriteSercice.update(permissionModifyReq);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<?> modify(@PathVariable @Positive Integer id){
        permissionWriteSercice.remove(id);
        return Result.ok();
    }




}

package com.acimage.admin.web.controller;


import com.acimage.admin.model.request.ApiAddReq;
import com.acimage.admin.model.request.ApiModifyReq;
import com.acimage.admin.service.api.ApiWriteService;
import com.acimage.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@Slf4j
@RequestMapping("/api/admin/apis/operate")
@Validated
public class ApiOperateController {
    @Autowired
    ApiWriteService apiWriteService;

    @PostMapping
    public Result<?> addApi(@Validated @RequestBody ApiAddReq apiAddreq){
        String path=apiAddreq.getPath().trim();
        if(path.length()<2){
            return Result.fail("路径有效长度不少于2");
        }
        apiAddreq.setPath(path);
        apiWriteService.save(apiAddreq);
        return Result.ok();
    }

    @PutMapping
    public Result<?> modifyApi(@Validated @RequestBody ApiModifyReq apiModifyReq){
        String path=apiModifyReq.getPath().trim();
        if(path.length()<2){
            return Result.fail("路径有效长度不少于2");
        }
        apiModifyReq.setPath(path);
        apiWriteService.update(apiModifyReq);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteApi(@Positive @NotNull @PathVariable int id){
        apiWriteService.delete(id);
        return Result.ok();
    }
}

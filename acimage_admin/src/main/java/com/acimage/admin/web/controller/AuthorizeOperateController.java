package com.acimage.admin.web.controller;


import com.acimage.admin.service.authorize.AuthorizeQueryService;
import com.acimage.admin.service.authorize.AuthorizeWriteService;
import com.acimage.common.model.domain.Authorize;
import com.acimage.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/admin/authorizes")
@Validated
public class AuthorizeOperateController {
    @Autowired
    AuthorizeWriteService authorizeWriteService;

    @PostMapping
    public Result<?> addAuthorize(@RequestParam @Positive Integer roleId,
                                  @RequestParam @Positive Integer permissionId){
        authorizeWriteService.save(roleId,permissionId);
        return Result.ok();
    }

    @DeleteMapping("/{roleId}/{permissionId}")
    public Result<?> deleteAuthorize(@PathVariable @Positive Integer roleId,
                                  @PathVariable @Positive Integer permissionId){
        authorizeWriteService.remove(roleId,permissionId);
        return Result.ok();
    }

}

package com.acimage.admin.web.controller;


import com.acimage.admin.service.authorize.AuthorizeQueryService;
import com.acimage.common.model.domain.sys.Authorize;
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
public class AuthorizeQueryController {
    @Autowired
    AuthorizeQueryService authorizeQueryService;

    @GetMapping("/roleId/{roleId}")
    public Result<List<Authorize>> queryRoleAuthorize(@PathVariable @Positive Integer roleId){
        return Result.ok(authorizeQueryService.listAuthorizeByRoleId(roleId));
    }

}

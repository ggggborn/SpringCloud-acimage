package com.acimage.admin.web.controller;


import cn.hutool.core.util.StrUtil;
import com.acimage.admin.service.authorize.AuthorizeQueryService;
import com.acimage.admin.service.homecarousel.HomeCarouselQueryService;
import com.acimage.admin.service.homecarousel.HomeCarouselWriteService;
import com.acimage.common.global.consts.FileFormat;
import com.acimage.common.model.domain.Authorize;
import com.acimage.common.model.domain.HomeCarousel;
import com.acimage.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
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

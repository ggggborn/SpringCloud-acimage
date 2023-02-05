package com.acimage.admin.web.controller;


import com.acimage.admin.model.request.ApiQueryReq;
import com.acimage.admin.service.api.ApiQueryService;
import com.acimage.common.model.domain.sys.Api;
import com.acimage.common.model.page.MyPage;
import com.acimage.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/admin/apis/query")
@Validated
public class ApiQueryController {
    @Autowired
    ApiQueryService apiQueryService;

    @GetMapping("/search")
    public Result<MyPage<Api>> queryApisBy(@Validated @ModelAttribute ApiQueryReq apiQueryReq){
        return Result.ok(apiQueryService.pageBy(apiQueryReq));
    }
}

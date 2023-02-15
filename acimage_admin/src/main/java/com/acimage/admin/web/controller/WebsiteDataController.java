package com.acimage.admin.web.controller;


import com.acimage.admin.model.vo.WebsiteDataVo;
import com.acimage.admin.service.websitedata.WebsiteDataService;
import com.acimage.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/admin/websites")
@Validated
public class WebsiteDataController {
    @Autowired
    WebsiteDataService websiteDataService;

    @GetMapping("/accessData")
    public Result<WebsiteDataVo> queryWebsiteData() {
        return Result.ok(websiteDataService.getWebsiteData());
    }
}

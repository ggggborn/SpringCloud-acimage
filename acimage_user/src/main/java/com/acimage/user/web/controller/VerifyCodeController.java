package com.acimage.user.web.controller;


import com.acimage.common.result.Result;
import com.acimage.user.service.verify.VerifyCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@RestController
@Validated
@RequestMapping("/api/user/verifies")
public class VerifyCodeController {
    @Autowired
    VerifyCodeService verifyCodeService;


    @GetMapping("/commonCode")
    public Result getCommonVerifyCode(HttpServletRequest request,HttpServletResponse response) {
        verifyCodeService.writeCodeImageToResponseAndRecord(request,response);
        return Result.ok();
    }



}

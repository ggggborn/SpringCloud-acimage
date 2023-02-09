package com.acimage.admin.web.controller;


import com.acimage.admin.model.request.AdminLoginReq;
import com.acimage.admin.service.login.LoginService;
import com.acimage.admin.service.login.VerifyCodeService;
import com.acimage.common.result.Result;
import com.acimage.common.utils.RsaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@RequestMapping("/api/admin/logins")
@Validated
public class AdminLoginController {
    @Autowired
    LoginService loginService;
    @Autowired
    VerifyCodeService verifyCodeService;
    @PostMapping("/doLogin")
    public Result<?> doLogin(@Validated @RequestBody AdminLoginReq adminLoginReq, HttpServletRequest request) {
        String code = adminLoginReq.getVerifyCode();
        boolean verifyCorrect = verifyCodeService.verifyAndRemoveIfSuccess(request, code);
        if (!verifyCorrect) {
            return Result.fail("验证码错误，请重新验证");
        }
        loginService.login(adminLoginReq);
        return Result.ok();
    }

    @GetMapping("/publicKey")
    public Result<?> getPublicKey() {
        return Result.ok(RsaUtils.getPublicKey());
    }


    @GetMapping("/commonCode")
    public Result<?> getCommonVerifyCode(HttpServletRequest request, HttpServletResponse response) {
        verifyCodeService.writeCodeImageToResponseAndRecord(request,response);
        return Result.ok();
    }
}

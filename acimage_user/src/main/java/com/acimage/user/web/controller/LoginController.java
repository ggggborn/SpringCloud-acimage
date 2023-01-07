package com.acimage.user.web.controller;


import com.acimage.common.global.annotation.Authentication;
import com.acimage.common.global.enums.AuthenticationType;
import com.acimage.common.result.Result;

import com.acimage.user.model.request.UserLoginReq;
import com.acimage.user.model.request.UserRegisterReq;
import com.acimage.user.service.user.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@RestController
@Authentication(type = AuthenticationType.NONE)
@Validated
@RequestMapping("/api/user/logins")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @GetMapping("/getPublicKey")
    public Result getPublicKey() {
        return Result.ok(loginService.getPublicKey());
    }

    @PostMapping("/doRegister")
    public Result register(@Validated @RequestBody UserRegisterReq userRegister, HttpServletResponse resp) {
        String token = loginService.registerUser(userRegister, resp);
        return Result.ok(token);
    }

    @PostMapping("/doLogin")
    public Result login(@Validated @RequestBody UserLoginReq userLogin, HttpServletResponse resp) {
        String token = loginService.login(userLogin, resp);
        return Result.ok(token);
    }

    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        loginService.logout(request);
        return Result.ok();
    }

}

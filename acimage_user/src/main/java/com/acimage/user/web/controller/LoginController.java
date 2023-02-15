package com.acimage.user.web.controller;


import com.acimage.common.deprecated.annotation.Authentication;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.global.enums.AuthenticationType;
import com.acimage.common.model.domain.user.User;
import com.acimage.common.redis.annotation.RequestLimit;
import com.acimage.common.redis.enums.LimitTarget;
import com.acimage.common.result.Result;

import com.acimage.user.model.request.UserLoginReq;
import com.acimage.user.model.request.UserRegisterReq;
import com.acimage.user.service.user.LoginService;
import com.acimage.user.service.user.UserQueryService;
import com.acimage.user.service.verify.VerifyCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;


@Slf4j
@RestController
@Authentication(type = AuthenticationType.NONE)
@Validated
@RequestMapping("/api/user/logins")
public class LoginController {
    @Autowired
    VerifyCodeService verifyCodeService;
    @Autowired
    LoginService loginService;
    @Autowired
    UserQueryService userQueryService;



    @RequestLimit(limitTimes = {1}, durations = {1}, penaltyTimes = {-1}, targets = {LimitTarget.IP})
    @GetMapping("/isExist/{username}")
    public Result<Boolean> isUsernameExist(@Size(min = 2, max = 12, message = "用户名长度在2到12之间") @PathVariable String username) {
        return Result.ok(userQueryService.isUsernameExist(username));
    }


    @GetMapping("/getPublicKey")
    public Result<?> getPublicKey() {
        return Result.ok(loginService.getPublicKey());
    }

    @RequestLimit(limitTimes = {1}, durations = {2}, penaltyTimes = {-1}, targets = {LimitTarget.IP})
    @PostMapping("/sendCode")
    public Result<?> sendVerifyCodeToEmail(@Email @Size(min=6,max=32,message = "邮箱长度在6到32之间") @RequestParam("email") String email,
                                           HttpServletRequest request) {
        loginService.checkAndSendCodeToEmail(email);
        return Result.ok();
    }

    @RequestLimit(limitTimes = {1}, durations = {2}, penaltyTimes = {-1}, targets = {LimitTarget.IP})
    @PostMapping("/doRegister")
    public Result<?> register(@Validated @RequestBody UserRegisterReq userRegister) {
        String username=userRegister.getUsername().trim();
        String code=userRegister.getVerifyCode().trim();
        String email= userRegister.getEmail().trim();
        userRegister.setVerifyCode(code);
        userRegister.setEmail(email);
        userRegister.setUsername(username);

        if(username.length()<2){
            return Result.fail("用户名有效长度不能少于2");
        }

        if(code.length()!=UserRegisterReq.VERIFY_CODE_LENGTH){
            return Result.fail("邮箱验证码错误");
        }
        boolean verifyCorrect =verifyCodeService.verifyEmailByVerifyCode(email,code);
        if(!verifyCorrect){
            log.warn("邮箱验证码错误 ip:{} 邮箱:{}", UserContext.getIp(),email);
            return Result.fail("邮箱验证码错误");
        }
        String token = loginService.registerUser(userRegister);
        return Result.ok(token);
    }

    @RequestLimit(limitTimes = {1}, durations = {2}, penaltyTimes = {-1}, targets = {LimitTarget.IP})
    @PostMapping("/doLogin")
    public Result<?> login(@Validated @RequestBody UserLoginReq userLogin, HttpServletRequest request) {
        String code = userLogin.getVerifyCode();
        boolean verifyCorrect = verifyCodeService.verifyAndRemoveIfSuccess(request, code);
        if (!verifyCorrect) {
            return Result.fail("验证码错误，请重新验证");
        }

        String token = loginService.login(userLogin);
        return Result.ok(token);
    }

    @PostMapping("/logout")
    public Result<?> logout(HttpServletRequest request) {
        loginService.logout(request);
        return Result.ok();
    }

}

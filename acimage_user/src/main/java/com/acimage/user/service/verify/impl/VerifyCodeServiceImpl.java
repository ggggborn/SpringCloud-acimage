package com.acimage.user.service.verify.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.util.RandomUtil;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.user.service.mail.MainService;
import com.acimage.user.service.verify.VerifyCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {
    public static final String STRINGKP_VERIFY_CODE = "acimage:user:verifyCode:sessionId:";
    public static final String STRINGKP_EMAIL_VERIFY = "acimage:user:logins:verify:email";
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    MainService mainService;

    @Override
    public void writeCodeImageToResponseAndRecord(HttpServletRequest request, HttpServletResponse response) {
        int width = 100;
        int height = 40;
        int codeCount = 4;
        int thickness = 4;
        /**
         * 注意验证码需要系统有默认字体，如果使用docker alpine，会因为没有默认字体而出错
         */
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(width, height, codeCount, thickness);
        //图形验证码写出，可以写出到文件，也可以写出到流
        ServletOutputStream outputStream=null;
        try {
            outputStream = response.getOutputStream();
            captcha.write(outputStream);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        //获取验证码中的文字内容
        String verifyCode = captcha.getCode();
        //记录到redis
        String sessionId = request.getSession().getId();

        long timeout = 30L;

        redisUtils.setAsString(STRINGKP_VERIFY_CODE + sessionId, verifyCode, timeout, TimeUnit.SECONDS);



    }

    @Override
    public boolean verifyAndRemoveIfSuccess(HttpServletRequest request, String code) {
        String key = STRINGKP_VERIFY_CODE + request.getSession().getId();

        String trueCode = redisUtils.getForString(key);
        if (trueCode == null) {
            return false;
        }

        if (trueCode.equals(code)) {
            redisUtils.delete(key);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void sendVerifyCodeToEmail(String email, int length) {
        String code = RandomUtil.randomString(length);
        int timeoutMinute = 3;
        mainService.sendVerifyCodeMailMessage(email, code, timeoutMinute);
        String key = STRINGKP_EMAIL_VERIFY + email;
        redisUtils.setAsString(key, code, timeoutMinute, TimeUnit.MINUTES);
    }

    @Override
    public boolean verifyEmailByVerifyCode(String email, String code) {
        String key = STRINGKP_EMAIL_VERIFY + email;
        String trueCode = redisUtils.getForString(key);
        if (trueCode == null) {
            return false;
        }

        if (trueCode.equals(code)) {
            redisUtils.delete(key);
            return true;
        } else {
            return false;
        }
    }
}

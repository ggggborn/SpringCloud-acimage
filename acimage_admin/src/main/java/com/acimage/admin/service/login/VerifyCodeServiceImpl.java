package com.acimage.admin.service.login;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.acimage.common.utils.redis.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class VerifyCodeServiceImpl implements VerifyCodeService{
    @Autowired
    RedisUtils redisUtils;
    public static final String STRINGKP_VERIFY_CODE="acimage:admin:verifyCode:sessionId:";

    @Override
    public void writeCodeImageToResponseAndRecord(HttpServletRequest request, HttpServletResponse response){
        int width=100;
        int height=40;
        int codeCount=4;
        int thickness=4;
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(width, height, codeCount, thickness);
        //图形验证码写出，可以写出到文件，也可以写出到流
        try {
            captcha.write(response.getOutputStream());
        } catch (IOException e) {
            log.error("response.getOutputStream()错误 {}",e.getMessage());
            throw new RuntimeException(e);
        }
        //获取验证码中的文字内容
        String verifyCode = captcha.getCode();
        //记录到redis
        String sessionId=request.getSession().getId();
        long timeout=30L;
        redisUtils.setAsString(STRINGKP_VERIFY_CODE+sessionId,verifyCode,timeout, TimeUnit.SECONDS);
    }

    @Override
    public boolean verifyAndRemoveIfSuccess(HttpServletRequest request, String code){
        String key=STRINGKP_VERIFY_CODE+request.getSession().getId();

        String trueCode=redisUtils.getForString(key);
        if(trueCode==null){
            return false;
        }

        if(trueCode.equals(code)){
            redisUtils.delete(key);
            return true;
        }else{
            return false;
        }
    }
}

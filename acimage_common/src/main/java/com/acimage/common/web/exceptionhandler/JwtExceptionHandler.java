package com.acimage.common.web.exceptionhandler;


import com.acimage.common.utils.SpringContextUtils;
import com.acimage.common.utils.common.PageUtils;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.acimage.common.result.Code;
import com.acimage.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class JwtExceptionHandler {
    @ExceptionHandler(value={JWTVerificationException.class})
    public Result doTokenException(JWTVerificationException ex){
        log.error("{}",ex.getMessage());
        return new Result(Code.TOKEN_ERR,null,"登录失效，请重新登录");
    }
}

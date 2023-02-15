package com.acimage.common.web.interceptor;


import com.acimage.common.global.consts.HeaderKeyConstants;
import com.acimage.common.global.exception.NullTokenException;
import com.acimage.common.utils.IpUtils;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.service.TokenService;
import com.acimage.common.utils.JwtUtils;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 获取请求的token状态
 */
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String ip = IpUtils.getIp(request);
        UserContext.setIp(ip);
        String token = request.getHeader(HeaderKeyConstants.AUTHORIZATION);

        try {
            JwtUtils.verifyToken(token);
        } catch (TokenExpiredException e1) {
            Date date = JwtUtils.getExpire(token);
            //过时毫秒数
            long expireMillis = System.currentTimeMillis() - date.getTime();
            //过时限制不超过10s,可以继续解析token
            long boundMillis = 10 * 1000;
            if (expireMillis > boundMillis) {
                return true;
            }
        } catch (JWTVerificationException e2) {
            if (!(e2 instanceof NullTokenException)) {
                log.warn("ip:{}非法token", ip);
            }
            return true;
        }

        if (!tokenService.hasRecorded(token)) {
            return true;
        }

        //设置当前用户信息
        UserContext.saveCurrentUserInfo(JwtUtils.getUserId(token), JwtUtils.getUsername(token), JwtUtils.getPhotoUrl(token));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.remove();
    }
}

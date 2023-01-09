package com.acimage.common.web.interceptor;

import com.acimage.common.global.consts.HeaderKey;
import com.acimage.common.utils.IpUtils;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.service.TokenService;
import com.acimage.common.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        String token = request.getHeader(HeaderKey.AUTHORIZATION);;

        try {
            JwtUtils.verifyToken(token);
        } catch (JWTVerificationException e1) {
            return true;
        }

        if (!tokenService.isMatch(token, ip)) {
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

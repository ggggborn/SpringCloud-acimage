package com.acimage.common.web.interceptor;

import com.acimage.common.global.context.UserContext;
import com.acimage.common.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 限流拦截
 */

@Slf4j
public class RequestLimitInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = IpUtils.getIp(request);
        UserContext.setIp(ip);

        log.info("access 用户:{} 访问:{} {} ip:{}",
                UserContext.getUsername(), request.getRequestURI(), request.getMethod(), ip);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView
            modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }


}
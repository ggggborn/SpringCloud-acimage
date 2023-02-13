package com.acimage.common.web.interceptor;

import com.acimage.common.global.context.UserContext;
import com.acimage.common.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 验证token状态和请求所需权限是否匹配
 */

@Slf4j
public class AccessInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = IpUtils.getIp(request);
        UserContext.setIp(ip);

        log.debug("access 用户:{} 访问:{} {} ip:{}",
                UserContext.getUsername(), request.getRequestURI(), request.getMethod(), ip);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView
            modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception
            ex) throws Exception {
        //移除用户信息，防止之后用该线程的信息误判（因为线程池）
        UserContext.remove();
    }
}

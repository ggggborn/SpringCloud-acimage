package com.acimage.common.web.interceptor.depreted;

import com.acimage.common.deprecated.annotation.utils.AuthenticationUtils;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.global.enums.AuthenticationType;
import com.acimage.common.global.enums.TokenStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 验证token状态和请求所需权限是否匹配
 */

@Slf4j
public class PermissionInterceptorBak implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = UserContext.getIp();

        log.info("access 用户:{} token状态:{} 访问:{} {} ip:{}",
                UserContext.getUsername(), UserContext.getTokenStatus(), request.getRequestURI(), request.getMethod(), ip);

        TokenStatus tokenStatus = UserContext.getTokenStatus();
        if (tokenStatus == TokenStatus.PASS_TOKEN_VERIFY) {
            return true;
        }
        //如果通过viewController访问页面
        if (handler instanceof ParameterizableViewController) {
            if (!(tokenStatus == TokenStatus.VALID||tokenStatus == TokenStatus.MISMATCH_IP)) {

                response.sendRedirect("/");
                return false;
            }
        }

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取该方法权限类型，优先注解顺序：方法、类
            AuthenticationType authenticationType = AuthenticationUtils.getAuthenticationType(handlerMethod);
            //无登录权限要求则放行
            if (authenticationType == null || authenticationType == AuthenticationType.NONE) {
                return true;
            } else if (authenticationType == AuthenticationType.TOKEN_REQUIRED) {
                if (!(tokenStatus == TokenStatus.VALID||tokenStatus == TokenStatus.MISMATCH_IP)) {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return false;
                }
            }
        }
//        ParameterizableViewController viewHandler=(ParameterizableViewController) handler;
//        viewHandler.getViewName();
//        String token= CookieUtils.getValueByName(request.getCookies(), JwtUtils.KEY_TOKEN);

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

package com.acimage.common.web.interceptor.depreted;

import com.acimage.common.exception.NullTokenException;
import com.acimage.common.global.annotation.utils.AuthenticationUtils;
import com.acimage.common.global.consts.HeaderKeyConstants;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.global.enums.AuthenticationType;
import com.acimage.common.global.enums.TokenStatus;
import com.acimage.common.service.TokenService;
import com.acimage.common.utils.IpUtils;
import com.acimage.common.utils.JwtUtils;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 获取请求的token状态
 */
public class JwtInterceptorBak implements HandlerInterceptor {
    @Autowired
    TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //设置ip
        String ip = IpUtils.getIp(request);
        UserContext.setIp(ip);
        
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod=(HandlerMethod) handler;
            //获取该方法权限类型，优先注解顺序：方法、类
            AuthenticationType authenticationType = AuthenticationUtils.getAuthenticationType(handlerMethod);
            //无登录权限要求则放行
            if (authenticationType == null || authenticationType == AuthenticationType.NONE) {
                UserContext.setTokenStatus(TokenStatus.PASS_TOKEN_VERIFY);
                return true;
            }
        }

        //获取token
        String token = request.getHeader(HeaderKeyConstants.AUTHORIZATION);

        //验证token，验证不通过抛出异常
        try {
            JwtUtils.verifyToken(token);
        } catch (NullTokenException e1) {
            UserContext.setTokenStatus(TokenStatus.NULL);
            return true;
        } catch (TokenExpiredException e2) {
            UserContext.setTokenStatus(TokenStatus.EXPIRE);
            return true;
        } catch (JWTVerificationException e3) {
            UserContext.setTokenStatus(TokenStatus.INVALID);
            return true;
        }

        if (!tokenService.isMatch(token, ip)) {
            UserContext.setTokenStatus(TokenStatus.MISMATCH_IP);
        } else {
            UserContext.setTokenStatus(TokenStatus.VALID);
        }

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

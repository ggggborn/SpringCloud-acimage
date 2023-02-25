package com.acimage.user.web.websocket;

import com.acimage.common.global.consts.HeaderKeyConstants;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.global.exception.NullTokenException;
import com.acimage.common.service.TokenService;
import com.acimage.common.utils.IpUtils;
import com.acimage.common.utils.JwtUtils;
import com.acimage.user.global.consts.WebSocketSessionConstants;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.*;

@Slf4j
@Component
public class MyHandshakeInterceptor implements HandshakeInterceptor {
    @Autowired
    TokenService tokenService;

    /**
     * 握手之前，若返回false，则不建立链接 *
     *
     * @return
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {

        //将用户id放入socket处理器的会话(WebSocketSession)中
        ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
        //获取参数
        String token = serverHttpRequest.getServletRequest().getHeader(HeaderKeyConstants.SEC_WEBSOCKET_PROTOCOL);
        try {
            JwtUtils.verifyToken(token);
        } catch (TokenExpiredException e1) {
            Date date = JwtUtils.getExpire(token);
            //过时毫秒数
            long expireMillis = System.currentTimeMillis() - date.getTime();
            //过时限制不超过10s,可以继续解析token
            long boundMillis = 10 * 1000;
            if (expireMillis > boundMillis) {
                return false;
            }
        } catch (JWTVerificationException e2) {
            if (!(e2 instanceof NullTokenException)) {
                log.warn("非法token");
            }
            return false;
        }

        if (!tokenService.hasRecorded(token)) {
            return false;
        }

        //设置当前用户信息
        attributes.put(WebSocketSessionConstants.KEY_USER_ID, JwtUtils.getUserId(token));
        response.getHeaders().put(HeaderKeyConstants.SEC_WEBSOCKET_PROTOCOL, Collections.singletonList(token));
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse
            response, WebSocketHandler wsHandler, Exception exception) {

    }


}


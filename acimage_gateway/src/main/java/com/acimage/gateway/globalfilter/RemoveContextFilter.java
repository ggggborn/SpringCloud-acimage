package com.acimage.gateway.globalfilter;


import com.acimage.common.exception.NullTokenException;
import com.acimage.common.global.consts.HeaderKeyConstants;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.service.impl.TokenServiceImpl;
import com.acimage.common.utils.IpUtils;
import com.acimage.common.utils.JwtUtils;
import com.acimage.gateway.config.IgnoreUrlConfig;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Order(Integer.MAX_VALUE)
public class RemoveContextFilter implements GlobalFilter {

   @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        UserContext.remove();

        return chain.filter(exchange);

    }
}

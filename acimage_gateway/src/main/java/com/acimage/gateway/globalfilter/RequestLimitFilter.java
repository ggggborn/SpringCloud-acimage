package com.acimage.gateway.globalfilter;


import com.acimage.common.utils.redis.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Slf4j
@Order(5)
@Component
public class RequestLimitFilter implements GlobalFilter {
    @Autowired
    private RedisUtils redisUtils;

    public static final int TOTAL_LIMIT = 1000;
    public static final String STRINGK_TOTAL_LIMIT = "acimage:gateway:limit:total";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        int increment = 1;
        long timeoutSeconds = 1L;
        Long count = redisUtils.increment(STRINGK_TOTAL_LIMIT, increment);
        if (count == 1) {
            redisUtils.expire(STRINGK_TOTAL_LIMIT, timeoutSeconds, TimeUnit.SECONDS);
        } else if (count >= TOTAL_LIMIT) {
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);


    }
}

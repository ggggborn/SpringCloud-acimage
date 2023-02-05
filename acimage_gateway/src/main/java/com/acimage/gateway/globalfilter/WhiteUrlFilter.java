package com.acimage.gateway.globalfilter;

import com.acimage.common.utils.IpUtils;
import com.acimage.gateway.config.IgnoreUrlConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;

import org.springframework.core.annotation.Order;

import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Order(10)
@Component
public class WhiteUrlFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request=exchange.getRequest();
        String url=request.getURI().getPath();
        String ip= IpUtils.getUserIp(request);

        if(IgnoreUrlConfig.isIgnoreUrl(url)){
            String method=request.getMethodValue();

            log.info("access pass url:{} {} ip:{} ", url,method,ip);
            return chain.filter(exchange);
        }
        return chain.filter(exchange);
    }
}

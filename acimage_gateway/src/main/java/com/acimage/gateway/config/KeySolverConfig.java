package com.acimage.gateway.config;


import com.acimage.common.utils.IpUtils;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

@Configuration
public class KeySolverConfig {

    @Bean
    KeyResolver ipKeyResolver() {
        return exchange ->{
            ServerHttpRequest request=exchange.getRequest();
           return  Mono.just(IpUtils.getUserIp(request));
        };
    }
}

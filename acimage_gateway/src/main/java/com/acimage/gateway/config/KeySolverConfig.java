package com.acimage.gateway.config;


import com.acimage.common.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class KeySolverConfig {

    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange ->{
            ServerHttpRequest request=exchange.getRequest();
           return  Mono.just(IpUtils.getUserIp(request));
        };
    }


}

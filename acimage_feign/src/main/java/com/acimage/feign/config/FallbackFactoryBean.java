package com.acimage.feign.config;

import com.acimage.feign.fallback.ImageClientFallbackFactory;
import com.acimage.feign.fallback.UserClientFallbackFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



public class FallbackFactoryBean {

    @Bean
    public ImageClientFallbackFactory imageClientFallbackFactory(){
        return new ImageClientFallbackFactory();
    }

    @Bean
    public UserClientFallbackFactory userClientFallbackFactory(){
        return new UserClientFallbackFactory();
    }
}

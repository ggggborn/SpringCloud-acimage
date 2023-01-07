package com.acimage.community.web.config;


import com.acimage.common.web.interceptor.IpInterceptor;
import com.acimage.common.web.interceptor.JwtInterceptor;
import com.acimage.common.web.interceptor.PermissionInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    JwtInterceptor jwtInterceptor;
    @Autowired
    IpInterceptor ipInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> commonExcludePathPatterns = new ArrayList<>(
                Arrays.asList("/templates/**", "/static/**", "/", "/storage/**", "/favicon.ico", "/error"));

        registry.addInterceptor(ipInterceptor).addPathPatterns("/**").order(10);

        registry.addInterceptor(jwtInterceptor).addPathPatterns("/**")
                .excludePathPatterns(commonExcludePathPatterns).order(20);

        registry.addInterceptor(new PermissionInterceptor()).addPathPatterns("/**")
                .excludePathPatterns(commonExcludePathPatterns).order(30);
    }


}

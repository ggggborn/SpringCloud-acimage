package com.acimage.user.global.config;



import com.acimage.common.web.interceptor.JwtInterceptor;
import com.acimage.common.web.interceptor.AccessInterceptor;
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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> commonExcludePathPatterns = new ArrayList<>(
                Arrays.asList("/templates/**", "/static/**", "/", "/storage/**", "/favicon.ico", "/error"));

        registry.addInterceptor(jwtInterceptor).addPathPatterns("/**")
                .excludePathPatterns(commonExcludePathPatterns).order(20);

        registry.addInterceptor(new AccessInterceptor()).addPathPatterns("/**")
                .excludePathPatterns(commonExcludePathPatterns).order(30);
    }


}

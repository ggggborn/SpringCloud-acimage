package com.acimage.admin.config;

import com.acimage.common.web.interceptor.AccessInterceptor;
import com.acimage.common.web.interceptor.JwtInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {


        registry.addInterceptor(new AccessInterceptor()).addPathPatterns("/**").order(30);
    }


}

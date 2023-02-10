package com.acimage.community.global.config;



import com.acimage.common.web.interceptor.JwtInterceptor;
import com.acimage.common.web.interceptor.AccessInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(jwtInterceptor).addPathPatterns("/**").order(20);

        registry.addInterceptor(new AccessInterceptor()).addPathPatterns("/**").order(30);
    }


}

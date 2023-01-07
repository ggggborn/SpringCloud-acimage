package com.acimage.image.web.config;



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

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/templates/**").addResourceLocations("/templates/");
//        registry.addResourceHandler("/favicon.ico").addResourceLocations("/static/images/favicon.ico");
//        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
//        registry.addResourceHandler("/storage/images/**").addResourceLocations("/storage/images/");
//        registry.addResourceHandler("/storage/photos/**").addResourceLocations("/storage/photos/");
//    }
//
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("login");
//        registry.addViewController("/home").setViewName("home");
//        registry.addViewController("/share").setViewName("share");
//        registry.addViewController("/showTopic").setViewName("ShowTopic");
//        registry.addViewController("/profile").setViewName("profile");
//        registry.addViewController("/MyActivity").setViewName("MyActivity");
//    }

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

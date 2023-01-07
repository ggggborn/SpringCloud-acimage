package com.acimage.common.web.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<CharacterEncodingFilter> registerFilter1(){
        FilterRegistrationBean<CharacterEncodingFilter> registerFilter=new FilterRegistrationBean<>();
        CharacterEncodingFilter encodeFilter=new CharacterEncodingFilter();
        encodeFilter.setEncoding("UTF-8");
        registerFilter.setFilter(encodeFilter);
        registerFilter.addUrlPatterns("/*");
        return registerFilter;
    }

    @Bean
    public FilterRegistrationBean<HiddenHttpMethodFilter> registerFilter2(){
        FilterRegistrationBean<HiddenHttpMethodFilter> registerFilter=new FilterRegistrationBean<>();
        HiddenHttpMethodFilter hiddenHttpMethodFilter=new HiddenHttpMethodFilter();
        registerFilter.setFilter(hiddenHttpMethodFilter);
        registerFilter.addUrlPatterns("/*");
        return registerFilter;
    }
}

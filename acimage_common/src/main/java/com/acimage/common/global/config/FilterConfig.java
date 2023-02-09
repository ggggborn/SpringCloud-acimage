package com.acimage.common.global.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
public class FilterConfig {

    /**
     * 设置编码
     * @return
     */
    @Bean
    public FilterRegistrationBean<CharacterEncodingFilter> registerFilter1(){
        FilterRegistrationBean<CharacterEncodingFilter> registerFilter=new FilterRegistrationBean<>();
        CharacterEncodingFilter encodeFilter=new CharacterEncodingFilter();
        encodeFilter.setEncoding("UTF-8");
        registerFilter.setFilter(encodeFilter);
        registerFilter.addUrlPatterns("/*");
        return registerFilter;
    }

    /**
     * 设置HiddenHttpMethod，支持put、get、delete等请求
     * @return
     */
    @Bean
    public FilterRegistrationBean<HiddenHttpMethodFilter> registerFilter2(){
        FilterRegistrationBean<HiddenHttpMethodFilter> registerFilter=new FilterRegistrationBean<>();
        HiddenHttpMethodFilter hiddenHttpMethodFilter=new HiddenHttpMethodFilter();
        registerFilter.setFilter(hiddenHttpMethodFilter);
        registerFilter.addUrlPatterns("/*");
        return registerFilter;
    }
}

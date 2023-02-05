package com.acimage.admin.config.mybatis;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
@Deprecated
public class GlobalConfigBean {

    @Bean
    @ConfigurationProperties(prefix = "mybatis-plus.global-config")
    GlobalConfig globalConfig(){
        return new GlobalConfig();
    }
}

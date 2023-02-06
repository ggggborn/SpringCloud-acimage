package com.acimage.gateway;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.stereotype.Component;

@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.acimage.gateway.dao")
@ComponentScan(value = {"com.acimage.gateway","com.acimage.common.utils"})
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
        log.info("------------->>>Gateway启动<<<-------------");
    }

}

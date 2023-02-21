package com.acimage.gateway.apitree;


import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.gateway.serivce.ApiQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InitApiTreeApplicationRunner implements ApplicationRunner {

    @Autowired
    ApiQueryService apiQueryService;
    @Autowired
    ApiTreeFactory apiTreeFactory;


    @Override
    public void run(ApplicationArguments args) {
        log.info("开始初始化apiTree");
        ApiTree apiTree=ApiTreeUtils.buildApiTreeFrom(apiQueryService.listEnableApis());
        apiTreeFactory.setApiTree(apiTree);
        log.info("初始化api tree完成");

    }
}

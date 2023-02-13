package com.acimage.community.runner;

import com.acimage.community.service.topic.TopicPreheatService;
import com.acimage.community.global.enums.TopicAttribute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class PreheatApplicationRunner implements ApplicationRunner {

    @Autowired
    TopicPreheatService topicPreheatService;

    @Override
    public void run(ApplicationArguments args) {
        log.info("start 预热热点topic");
        int initialRankSize = 30;
        int cacheSize = 10;
        long expireSeconds = 3L;

        for (TopicAttribute attr : TopicAttribute.values()) {
            log.info("start 根据{}预热", attr);
            int size=initialRankSize;
            if(attr==TopicAttribute.ACTIVITY_TIME){
                size=1000;
            }
            topicPreheatService.preheatTopicsOrderBy(attr, size, cacheSize,
                    expireSeconds, TimeUnit.SECONDS);
        }
        log.info("end 预热热点topic");
    }
}

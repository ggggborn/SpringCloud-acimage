package com.acimage.community.init;

import com.acimage.common.model.Index.TopicIndex;
import com.acimage.common.utils.EsUtils;
import com.acimage.community.mq.producer.RemoveTopicImagesMqProducer;
import com.acimage.community.service.topic.TopicPreheatService;
import com.acimage.community.service.topic.enums.TopicAttribute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CreateIndexRunner implements ApplicationRunner {

    @Autowired
    EsUtils esUtils;


    @Override
    public void run(ApplicationArguments args) {
        esUtils.createIndexIfNotExist(TopicIndex.class);
    }
}

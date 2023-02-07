package com.acimage.community.runner;

import com.acimage.common.model.Index.TopicIndex;
import com.acimage.common.utils.EsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

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

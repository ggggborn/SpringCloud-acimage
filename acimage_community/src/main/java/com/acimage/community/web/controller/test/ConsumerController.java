package com.acimage.community.web.controller.test;

import com.acimage.common.model.domain.Star;
import com.acimage.community.mq.config.HashImageMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Slf4j
public class ConsumerController {

        @RabbitListener(queues = HashImageMqConfig.HASH_IMAGE_QUEUE)
        @RabbitHandler
        public void process(Star testMessage) {
            log.info("DirectReceiver消费者收到消息  : " + testMessage);
        }


}

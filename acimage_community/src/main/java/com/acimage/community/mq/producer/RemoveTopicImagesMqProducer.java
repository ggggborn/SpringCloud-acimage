package com.acimage.community.mq.producer;

import com.acimage.community.mq.config.RemoveTopicImagesMqConfig;
import com.acimage.community.global.consts.ExchangeConstants;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@ConditionalOnProperty(prefix = "my-config",name="enable-mq",havingValue = "true")
@Component
public class RemoveTopicImagesMqProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendRemoveTopicMessage(long topicId){

        rabbitTemplate.convertAndSend(ExchangeConstants.TOPIC_IMAGES_EXCHANGE,
                RemoveTopicImagesMqConfig.REMOVE_TOPIC_IMAGE_ROUTE, topicId);
    }
}

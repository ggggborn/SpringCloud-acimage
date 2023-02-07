package com.acimage.community.mq.producer;

import cn.hutool.core.collection.CollectionUtil;
import com.acimage.common.model.mq.dto.HashImagesUpdateDto;
import com.acimage.common.model.mq.dto.ImageIdWithUrl;
import com.acimage.community.global.consts.ExchangeConstants;
import com.acimage.community.mq.config.HashImageMqConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HashImageMqProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendUploadImageMessage(long imageId, String url) {
        ImageIdWithUrl imageIdWithUrl = new ImageIdWithUrl(imageId, url);
        rabbitTemplate.convertAndSend(ExchangeConstants.TOPIC_IMAGES_EXCHANGE, HashImageMqConfig.HASH_IMAGE_ROUTE, imageIdWithUrl);
    }

    public void sendHashImagesMessage(long topicId) {
        rabbitTemplate.convertAndSend(ExchangeConstants.TOPIC_IMAGES_EXCHANGE, HashImageMqConfig.HASH_IMAGE_ROUTE, topicId);
    }

    public void sendHashImagesMessage(HashImagesUpdateDto hashImagesUpdateDto) {
        if (!CollectionUtil.isEmpty(hashImagesUpdateDto.getAddImageUrls()) ||
                !CollectionUtil.isEmpty(hashImagesUpdateDto.getRemoveImageUrls())) {
            rabbitTemplate.convertAndSend(ExchangeConstants.TOPIC_IMAGES_EXCHANGE, HashImageMqConfig.HASH_IMAGE_ROUTE, hashImagesUpdateDto);
        }
    }
}

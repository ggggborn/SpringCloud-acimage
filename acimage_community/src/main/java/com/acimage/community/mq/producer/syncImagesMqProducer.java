package com.acimage.community.mq.producer;

import cn.hutool.core.collection.CollectionUtil;
import com.acimage.common.global.consts.MqConstants;
import com.acimage.common.model.mq.dto.SyncImagesUpdateDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class syncImagesMqProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;

//    public void sendUploadImageMessage(long imageId, String url) {
//        ImageIdWithUrl imageIdWithUrl = new ImageIdWithUrl(imageId, url);
//        rabbitTemplate.convertAndSend(MqConstants.TOPIC_IMAGES_EXCHANGE, MqConstants.HASH_IMAGE_ROUTE, imageIdWithUrl);
//    }
//
//    public void sendHashImagesMessage(long topicId) {
//        rabbitTemplate.convertAndSend(MqConstants.TOPIC_IMAGES_EXCHANGE, MqConstants.HASH_IMAGE_ROUTE, topicId);
//    }

    public void sendSyncImagesMessage(SyncImagesUpdateDto updateDto) {
        if (!CollectionUtil.isEmpty(updateDto.getAddImageUrls()) ||
                !CollectionUtil.isEmpty(updateDto.getRemoveImageUrls())) {
            rabbitTemplate.convertAndSend(MqConstants.TOPIC_IMAGES_EXCHANGE, MqConstants.SYNC_IMAGES_ROUTE, updateDto);
        }
    }

//    public void sendRemoveTopicMessage(long topicId){
//
//        rabbitTemplate.convertAndSend(MqConstants.TOPIC_IMAGES_EXCHANGE,
//                MqConstants.REMOVE_TOPIC_IMAGE_ROUTE, topicId);
//    }
}

package com.acimage.image.mq.consumer;


import cn.hutool.core.collection.CollectionUtil;
import com.acimage.common.model.domain.image.Image;
import com.acimage.common.utils.QiniuUtils;
import com.acimage.common.utils.common.ListUtils;
import com.acimage.image.service.image.ImageQueryService;
import com.acimage.image.service.image.ImageWriteService;
import com.acimage.image.service.imagehash.ImageHashWriteService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class RemoveTopicImagesConsumer {
    private static final String REMOVE_TOPIC_IMAGES_QUEUE = "remove-topic-images-queue";

    @Autowired
    QiniuUtils qiniuUtils;

    @Autowired
    ImageWriteService imageWriteService;

    @Autowired
    ImageHashWriteService imageHashWriteService;
    @Autowired
    ImageQueryService imageQueryService;


    @RabbitListener(queues = REMOVE_TOPIC_IMAGES_QUEUE)
    @RabbitHandler
    public void removeTopicImages(Channel channel, Message message, long topicId) {
        log.info("删除topicId:{}相关图片及数据",topicId);
        try {
            List<Image> imageList = imageQueryService.listImagesOrderById(topicId);
            if (CollectionUtil.isEmpty(imageList)) {
                return;
            }
            //删除数据
            imageWriteService.removeImages(topicId);
            imageHashWriteService.removeImageHashes(ListUtils.extract(Image::getId, imageList));

            //异步删除oss中存储的图片
            new Thread(() -> {
                qiniuUtils.batchDelete(ListUtils.extract(Image::getUrl, imageList));
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
            log.error("移除话题图片消费者任务失败 error:{} 对象：{}", e.getMessage(), topicId);

        } finally {
            String messageBody = new String(message.getBody());
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("移除话题图片ack失败 error:{} message:{}", e.getMessage(), messageBody);
                try {
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    log.error("移除话题图片reject失败 error:{} message:{}", ex.getMessage(), messageBody);
                }
            }
        }


    }


}

package com.acimage.image.mq.consumer;



import com.acimage.common.global.consts.MqConstants;
import com.acimage.common.model.mq.dto.SyncImagesUpdateDto;
import com.acimage.image.service.image.ImageMixWriteService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;


import javax.annotation.PostConstruct;
import java.io.*;


@Slf4j
@Configuration
@RabbitListener(queues = MqConstants.SYNC_IMAGES_QUEUE)
public class SyncImagesConsumer {

    public String tempDirectory;
    @Autowired
    ImageMixWriteService imageMixWriteService;


    @PostConstruct
    public void init() {
        tempDirectory = System.getProperty("user.dir") + "//temp";
        //创建目录
        File directory = new File(tempDirectory);
        if (directory.mkdir()) {
            log.info("创建临时目录：{}", tempDirectory);
        }
    }

    @RabbitHandler
    public void syncImages(Channel channel, Message message, SyncImagesUpdateDto updateDto) {
        log.info("开始同步话题图片");
        try {
            imageMixWriteService.updateImageAndHash(updateDto);
        } catch (Exception e) {
            log.error("同步图片消费者任务失败 error:{} data:{}", e.getLocalizedMessage(), updateDto);
        } finally {
            String messageBody = new String(message.getBody());
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("同步图片ack失败 error:{} message:{} data:{}", e.getMessage(), messageBody, updateDto);
                try {
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                } catch (IOException ex) {
                    e.printStackTrace();
                    log.error("同步图片reject失败 error:{} message:{} data:{}", e.getMessage(), messageBody, updateDto);
                }
            }
        }

    }


}

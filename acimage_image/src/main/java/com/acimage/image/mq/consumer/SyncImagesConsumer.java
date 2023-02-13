package com.acimage.image.mq.consumer;


import com.acimage.common.global.consts.MqConstants;
import com.acimage.common.model.mq.dto.SyncImagesUpdateDto;
import com.acimage.common.utils.ExceptionUtils;
import com.acimage.common.utils.SpringContextUtils;
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
    @Autowired
    ImageMixWriteService imageMixWriteService;

    @RabbitHandler
    public void syncImages(Channel channel, Message message, SyncImagesUpdateDto updateDto) {
        log.info("开始同步话题图片");
        try {
            imageMixWriteService.updateImageAndHash(updateDto);
        } catch (Exception e) {
            ExceptionUtils.printIfDev(e);
            log.error("同步图片消费者任务失败 error:{} data:{}", e.getLocalizedMessage(), updateDto);
        } finally {
            String messageBody = new String(message.getBody());
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                ExceptionUtils.printIfDev(e);
                log.error("同步图片ack失败 error:{} message:{} data:{}", e.getMessage(), messageBody, updateDto);
                try {
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                } catch (IOException ex) {
                    ExceptionUtils.printIfDev(e);
                    log.error("同步图片reject失败 error:{} message:{} data:{}", e.getMessage(), messageBody, updateDto);
                }
            }
        }

    }


}

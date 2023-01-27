package com.acimage.community.mq.consumer;

import com.acimage.common.model.mq.dto.EsAddDto;
import com.acimage.common.model.mq.dto.EsDeleteDto;
import com.acimage.common.model.mq.dto.EsUpdateDto;
import com.acimage.common.model.mq.dto.UserIdWithPhotoUrl;
import com.acimage.common.utils.EsUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RabbitListener(queues = "sync-es-queue")
public class SyncEsConsumer {

    @Autowired
    EsUtils esUtils;

    @RabbitHandler
    public void syncAdd(Channel channel, Message message, EsAddDto esAddDto) {
        log.info("同步es数据：{}", esAddDto);
        try {
            esUtils.save(esAddDto);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("同步es数据失败 error:{} data：{}", e.getMessage(), esAddDto);

        } finally {
            String messageBody = new String(message.getBody());
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("同步es数据ack失败 error:{} message:{}", e.getMessage(), messageBody);
                try {
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    log.error("同步es数据reject失败 error:{} message:{}", ex.getMessage(), messageBody);
                }
            }
        }
    }


    @RabbitHandler
    public void syncDelete(Channel channel, Message message, EsDeleteDto esDeleteDto) {
        log.info("同步es数据：{}", esDeleteDto);
        try {
            esUtils.remove(esDeleteDto);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("同步es数据失败 error:{} data：{}", e.getMessage(), esDeleteDto);

        } finally {
            String messageBody = new String(message.getBody());
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("同步es数据ack失败 error:{} message:{}", e.getMessage(), messageBody);
                try {
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    log.error("同步es数据reject失败 error:{} message:{}", ex.getMessage(), messageBody);
                }
            }
        }
    }

    @RabbitHandler
    public void syncUpdate(Channel channel, Message message, EsUpdateDto esUpdateDto) {
        log.info("同步es数据：{}", esUpdateDto);
        try {
            esUtils.update(esUpdateDto);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("同步es数据失败 error:{} data：{}", e.getMessage(), esUpdateDto);

        } finally {
            String messageBody = new String(message.getBody());
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("同步es数据ack失败 error:{} message:{}", e.getMessage(), messageBody);
                try {
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    log.error("同步es数据reject失败 error:{} message:{}", ex.getMessage(), messageBody);
                }
            }
        }
    }
}

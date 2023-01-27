package com.acimage.community.mq.consumer;


import com.acimage.common.model.domain.community.UserBasic;
import com.acimage.common.model.mq.dto.UserIdWithPhotoUrl;
import com.acimage.common.model.mq.dto.UserIdWithUsername;

import com.acimage.community.service.userbasic.UserBasicWriteService;
import com.acimage.community.service.userbasic.UserMixWriteService;
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
@RabbitListener(queues = "sync-user-queue")
public class SyncUserConsumer {

    @Autowired
    UserBasicWriteService userBasicWriteService;
    @Autowired
    UserMixWriteService userMixWriteService;


    @RabbitHandler
    public void syncUsername(Channel channel, Message message, UserIdWithUsername userIdWithUsername) {
        log.info("同步用户名：{}", userIdWithUsername);
        try {
            userBasicWriteService.updateUsername(userIdWithUsername.getUserId(), userIdWithUsername.getUsername());


        } catch (Exception e) {
            e.printStackTrace();
            log.error("同步用户名任务失败 error:{} 对象：{}", e.getMessage(), userIdWithUsername);

        } finally {
            String messageBody = new String(message.getBody());
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("同步用户名ack失败 error:{} message:{}", e.getMessage(), messageBody);
                try {
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    log.error("同步用户名reject失败 error:{} message:{}", ex.getMessage(), messageBody);
                }
            }
        }

    }


    @RabbitHandler
    public void syncPhotoUrl(Channel channel, Message message, UserIdWithPhotoUrl userIdWithPhotoUrl) {
        log.info("同步头像地址：{}", userIdWithPhotoUrl);
        try {
            userBasicWriteService.updatePhotoUrl(userIdWithPhotoUrl.getUserId(), userIdWithPhotoUrl.getPhotoUrl());

        } catch (Exception e) {
            e.printStackTrace();
            log.error("同步头像地址任务失败 error:{} 对象：{}", e.getMessage(), userIdWithPhotoUrl);

        } finally {
            String messageBody = new String(message.getBody());
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("同步头像地址ack失败 error:{} message:{}", e.getMessage(), messageBody);
                try {
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    log.error("同步头像地址reject失败 error:{} message:{}", ex.getMessage(), messageBody);
                }
            }
        }
    }

    @RabbitHandler
    public void addUser(Channel channel, Message message, UserBasic userBasic) {
        log.info("新增用户：{}", userBasic);
        try {
            userMixWriteService.addUserBasicAndUserCommunityStatistic(userBasic);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("新增用户 error:{} 对象：{}", e.getMessage(), userBasic);

        } finally {
            String messageBody = new String(message.getBody());
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("新增用户ack失败 error:{} message:{}", e.getMessage(), messageBody);
                try {
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    log.error("新增用户reject失败 error:{} message:{}", ex.getMessage(), messageBody);
                }
            }
        }
    }


}

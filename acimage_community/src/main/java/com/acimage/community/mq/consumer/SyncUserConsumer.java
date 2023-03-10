package com.acimage.community.mq.consumer;


import com.acimage.common.global.consts.MqConstants;
import com.acimage.common.model.Index.TopicIndex;
import com.acimage.common.model.domain.community.CmtyUser;
import com.acimage.common.model.mq.dto.EsUpdateByTermDto;
import com.acimage.common.model.mq.dto.UserIdWithPhotoUrl;
import com.acimage.common.model.mq.dto.UserIdWithUsername;

import com.acimage.common.utils.EsUtils;
import com.acimage.common.utils.ExceptionUtils;
import com.acimage.common.utils.LambdaUtils;
import com.acimage.common.utils.SpringContextUtils;
import com.acimage.community.service.cmtyuser.CmtyUserWriteService;
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
@RabbitListener(queues = MqConstants.SYNC_USER_QUEUE)
public class SyncUserConsumer {

    @Autowired
    CmtyUserWriteService cmtyUserWriteService;
    @Autowired
    EsUtils esUtils;

    @RabbitHandler
    public void syncUsername(Channel channel, Message message, UserIdWithUsername userIdWithUsername) {
        log.info("同步用户名：{}", userIdWithUsername);
        try {
            cmtyUserWriteService.updateUsername(userIdWithUsername.getUserId(), userIdWithUsername.getUsername());
            TopicIndex topicIndex = TopicIndex.builder()
                    .userId(userIdWithUsername.getUserId())
                    .username(userIdWithUsername.getUsername())
                    .build();
            EsUpdateByTermDto updateDto = new EsUpdateByTermDto();
            updateDto.with(topicIndex);
            String termColumn = LambdaUtils.columnOf(TopicIndex::getUserId);
            List<String> columns = LambdaUtils.columnsFrom(TopicIndex::getUsername);
            updateDto.setTermColumn(termColumn);
            updateDto.setColumns(columns);
            esUtils.UpdateByTerm(updateDto);

        } catch (Exception e) {
            ExceptionUtils.printIfDev(e);
            log.error("同步用户名任务失败 error:{} 对象：{}", e.getMessage(), userIdWithUsername);

        } finally {
            String messageBody = new String(message.getBody());
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                ExceptionUtils.printIfDev(e);
                log.error("同步用户名ack失败 error:{} message:{}", e.getMessage(), messageBody);
                try {
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                } catch (IOException ex) {
                    ExceptionUtils.printIfDev(e);
                    log.error("同步用户名reject失败 error:{} message:{}", ex.getMessage(), messageBody);
                }
            }
        }

    }


    @RabbitHandler
    public void syncPhotoUrl(Channel channel, Message message, UserIdWithPhotoUrl userIdWithPhotoUrl) {
        log.info("同步头像地址：{}", userIdWithPhotoUrl);
        try {
            cmtyUserWriteService.updatePhotoUrl(userIdWithPhotoUrl.getUserId(), userIdWithPhotoUrl.getPhotoUrl());
            TopicIndex topicIndex = TopicIndex.builder()
                    .userId(userIdWithPhotoUrl.getUserId())
                    .photoUrl(userIdWithPhotoUrl.getPhotoUrl())
                    .build();
            EsUpdateByTermDto updateDto = new EsUpdateByTermDto();
            updateDto.with(topicIndex);
            String termColumn = LambdaUtils.columnOf(TopicIndex::getUserId);
            List<String> columns = LambdaUtils.columnsFrom(TopicIndex::getPhotoUrl);
            updateDto.setTermColumn(termColumn);
            updateDto.setColumns(columns);
            esUtils.UpdateByTerm(updateDto);

        } catch (Exception e) {
            ExceptionUtils.printIfDev(e);
            log.error("同步头像地址任务失败 error:{} data:{}", e.getMessage(), userIdWithPhotoUrl);

        } finally {
            String messageBody = new String(message.getBody());
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                ExceptionUtils.printIfDev(e);
                log.error("同步头像地址ack失败 error:{} message:{}", e.getMessage(), messageBody);
                try {
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                } catch (IOException ex) {
                    ExceptionUtils.printIfDev(e);
                    log.error("同步头像地址reject失败 error:{} message:{}", ex.getMessage(), messageBody);
                }
            }
        }
    }

    @RabbitHandler
    public void addUser(Channel channel, Message message, CmtyUser cmtyUser) {
        log.info("新增用户：{}", cmtyUser);
        try {
            cmtyUserWriteService.save(cmtyUser);
        } catch (Exception e) {
            ExceptionUtils.printIfDev(e);
            log.error("新增用户 error:{} 对象：{}", e.getMessage(), cmtyUser);

        } finally {
            String messageBody = new String(message.getBody());
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                ExceptionUtils.printIfDev(e);
                log.error("新增用户ack失败 error:{} message:{}", e.getMessage(), messageBody);
                try {
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                } catch (IOException ex) {
                    ExceptionUtils.printIfDev(e);
                    log.error("新增用户reject失败 error:{} message:{}", ex.getMessage(), messageBody);
                }
            }
        }
    }


}

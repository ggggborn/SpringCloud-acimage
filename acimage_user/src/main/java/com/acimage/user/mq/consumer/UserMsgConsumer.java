package com.acimage.user.mq.consumer;

import com.acimage.common.global.consts.MqConstants;
import com.acimage.common.model.domain.user.CommentMsg;
import com.acimage.common.model.domain.user.UserMsg;
import com.acimage.common.model.mq.dto.EsAddDto;
import com.acimage.common.model.mq.dto.EsDeleteDto;
import com.acimage.common.model.mq.dto.EsUpdateByIdDto;
import com.acimage.common.utils.EsUtils;
import com.acimage.common.utils.ExceptionUtils;
import com.acimage.user.service.commentmsg.CommentMsgWriteService;
import com.acimage.user.service.usermsg.UserMsgQueryService;
import com.acimage.user.service.usermsg.UserMsgWriteService;
import com.acimage.user.web.websocket.MyWebSocketHandler;
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
@RabbitListener(queues = MqConstants.USER_MSG_QUEUE)
public class UserMsgConsumer {

    @Autowired
    UserMsgWriteService userMsgWriteService;
    @Autowired
    CommentMsgWriteService commentMsgWriteService;
    @Autowired
    MyWebSocketHandler webSocketHandler;

    @RabbitHandler
    public void commentMsg(Channel channel, Message message, CommentMsg commentMsg) {
        log.info("用户评论消息通知：{}", commentMsg);
        try {
            //插入用户消息
            commentMsgWriteService.insert(commentMsg);
            //评论消息+1
            userMsgWriteService.increaseMsg(commentMsg.getToUserId(), UserMsg::getCommentMsgCount, 1);
            //推送消息给用户
            webSocketHandler.sendMsgCount(commentMsg.getToUserId());

        } catch (Exception e) {
            ExceptionUtils.printIfDev(e);
            log.error("用户评论消息通知失败 error:{} data：{}", e.getMessage(), commentMsg);

        } finally {
            String messageBody = new String(message.getBody());
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                ExceptionUtils.printIfDev(e);
                log.error("用户评论消息通知失败 error:{} message:{}", e.getMessage(), messageBody);
                try {
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                } catch (IOException ex) {
                    ExceptionUtils.printIfDev(ex);
                    log.error("用户评论消息通知reject失败 error:{} message:{}", ex.getMessage(), messageBody);
                }
            }
        }
    }


}

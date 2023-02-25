package com.acimage.community.mq.producer;

import com.acimage.common.global.consts.MqConstants;
import com.acimage.common.model.domain.user.CommentMsg;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMsgMqProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendCommentMessage(CommentMsg commentMsg) {
        rabbitTemplate.convertAndSend(MqConstants.COMMUNITY_USER_EXCHANGE, MqConstants.USER_MSG_ROUTE, commentMsg);
    }

}

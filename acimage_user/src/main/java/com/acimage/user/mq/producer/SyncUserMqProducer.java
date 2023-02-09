package com.acimage.user.mq.producer;

import com.acimage.common.global.consts.MqConstants;
import com.acimage.common.model.domain.community.CmtyUser;
import com.acimage.common.model.mq.dto.UserIdWithPhotoUrl;
import com.acimage.common.model.mq.dto.UserIdWithUsername;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SyncUserMqProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendSyncUsernameMessage(UserIdWithUsername userIdWithUsername){
        rabbitTemplate.convertAndSend(MqConstants.COMMUNITY_USER_EXCHANGE, MqConstants.SYNC_USER_ROUTE,userIdWithUsername);
    }

    public void sendSyncUserPhotoUrlMessage(UserIdWithPhotoUrl userIdWithPhotoUrl){
        rabbitTemplate.convertAndSend(MqConstants.COMMUNITY_USER_EXCHANGE, MqConstants.SYNC_USER_ROUTE,userIdWithPhotoUrl);
    }

    public void sendAddUserMessage(CmtyUser cmtyUser){
        rabbitTemplate.convertAndSend(MqConstants.COMMUNITY_USER_EXCHANGE, MqConstants.SYNC_USER_ROUTE, cmtyUser);
    }


}

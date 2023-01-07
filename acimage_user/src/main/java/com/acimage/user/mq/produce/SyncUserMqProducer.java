package com.acimage.user.mq.produce;

import com.acimage.common.model.domain.UserBasic;
import com.acimage.common.model.mq.dto.ImageIdWithUrl;
import com.acimage.common.model.mq.dto.UserIdWithPhotoUrl;
import com.acimage.common.model.mq.dto.UserIdWithUsername;
import com.acimage.user.mq.config.SyncUserMqConfig;
import com.acimage.user.mq.consts.ExchangeConstants;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SyncUserMqProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendSyncUsernameMessage(UserIdWithUsername userIdWithUsername){
        rabbitTemplate.convertAndSend(ExchangeConstants.COMMUNITY_USER_EXCHANGE, SyncUserMqConfig.SYNC_USER_ROUTE,userIdWithUsername);
    }

    public void sendSyncUserPhotoUrlMessage(UserIdWithPhotoUrl userIdWithPhotoUrl){
        rabbitTemplate.convertAndSend(ExchangeConstants.COMMUNITY_USER_EXCHANGE, SyncUserMqConfig.SYNC_USER_ROUTE,userIdWithPhotoUrl);
    }

    public void sendAddUserMessage(UserBasic userBasic){
        rabbitTemplate.convertAndSend(ExchangeConstants.COMMUNITY_USER_EXCHANGE, SyncUserMqConfig.SYNC_USER_ROUTE,userBasic);
    }


}

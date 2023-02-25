package com.acimage.community.mq.config;

import com.acimage.common.global.consts.MqConstants;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserMsgMqConfig {
    @Autowired
    AmqpAdmin rabbitAdmin;

    @Bean
    public Queue userMsgQueue() {
        return new Queue(MqConstants.USER_MSG_QUEUE, true);
    }

    @Bean
    DirectExchange communityUserExchange() {
        return new DirectExchange(MqConstants.COMMUNITY_USER_EXCHANGE, true, false);
    }

    //绑定  将队列和交换机绑定, 并设置用于匹配键
    @Bean
    Binding bindingUserMsg() {
        return BindingBuilder.bind(userMsgQueue()).to(communityUserExchange()).with(MqConstants.USER_MSG_ROUTE);
    }


    //创建交换机和队列
    @Bean
    public void createExchangeQueueForUserMsg() {
        rabbitAdmin.declareExchange(communityUserExchange());
        rabbitAdmin.declareQueue(userMsgQueue());
    }

}

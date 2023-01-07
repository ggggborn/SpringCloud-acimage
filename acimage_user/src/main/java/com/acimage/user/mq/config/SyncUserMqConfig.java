package com.acimage.user.mq.config;


import com.acimage.user.mq.consts.ExchangeConstants;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SyncUserMqConfig {
    public static final String SYNC_USER_QUEUE = "sync-user-queue";
    public static final String SYNC_USER_ROUTE = "sync-user-route";

    @Autowired
    AmqpAdmin rabbitAdmin;
    @Bean
    public Queue syncUserQueue() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        //   return new Queue("TestDirectQueue",true,true,false);

        //一般设置一下队列的持久化就好,其余两个就是默认false
        return new Queue(SYNC_USER_QUEUE, true);
    }


    @Bean
    DirectExchange syncUserExchange() {
        return new DirectExchange(ExchangeConstants.COMMUNITY_USER_EXCHANGE, true, false);
    }

    //绑定  将队列和交换机绑定, 并设置用于匹配键
    @Bean
    Binding syncUserBinding() {
        return BindingBuilder.bind(syncUserQueue()).to(syncUserExchange()).with(SYNC_USER_ROUTE);
    }

    //创建交换机和对列
    @Bean
    public void createExchangeQueueForSyncUser() {
        rabbitAdmin.declareExchange(syncUserExchange());
        rabbitAdmin.declareQueue(syncUserQueue());
    }

}

package com.acimage.community.mq.config;

import com.acimage.community.global.consts.ExchangeConstants;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RemoveTopicImagesMqConfig {
    public static final String REMOVE_TOPIC_IMAGES_QUEUE = "remove-topic-images-queue";
    public static final String REMOVE_TOPIC_IMAGE_ROUTE = "remove-topic-images-route";
    @Autowired
    AmqpAdmin rabbitAdmin;

    @Bean
    public Queue removeTopicImagesQueue() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        //   return new Queue("TestDirectQueue",true,true,false);

        //一般设置一下队列的持久化就好,其余两个就是默认false
        return new Queue(REMOVE_TOPIC_IMAGES_QUEUE, true,false,false);
    }


    @Bean
    DirectExchange topicImagesExchange() {
        //  return new DirectExchange("TestDirectExchange",true,true);
        return new DirectExchange(ExchangeConstants.TOPIC_IMAGES_EXCHANGE, true, false);
    }

    //绑定  将队列和交换机绑定, 并设置用于匹配键
    @Bean
    Binding binding1() {
        return BindingBuilder.bind(removeTopicImagesQueue()).to(topicImagesExchange()).with(REMOVE_TOPIC_IMAGE_ROUTE);
    }

    //创建交换机和对列
    @Bean
    public void createExchangeQueue1() {
        rabbitAdmin.declareExchange(topicImagesExchange());
        rabbitAdmin.declareQueue(removeTopicImagesQueue());
    }

}

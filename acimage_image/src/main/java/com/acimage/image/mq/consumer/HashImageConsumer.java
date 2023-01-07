package com.acimage.image.mq.consumer;


import com.acimage.common.model.domain.Image;
import com.acimage.common.model.mq.dto.ImageIdWithUrl;
import com.acimage.common.utils.QiniuUtils;
import com.acimage.image.service.image.ImageQueryService;
import com.acimage.image.service.imagehash.SearchImageService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.List;

@Slf4j
@Configuration
public class HashImageConsumer {
    private static final String HASH_IMAGE_QUEUE = "hash-image-queue";

    public String tempDirectory;
    @Autowired
    QiniuUtils qiniuUtils;
    @Autowired
    SearchImageService searchImageService;
    @Autowired
    ImageQueryService imageQueryService;

    @PostConstruct
    public void init() {
        tempDirectory=System.getProperty("user.dir")+"//temp";
        System.out.println(tempDirectory);

        //创建目录
        File directory = new File(tempDirectory);
        directory.mkdir();
    }

    @RabbitListener(queues = HASH_IMAGE_QUEUE)
    @RabbitHandler
    public void hashImageFromQiniu(Channel channel, Message message, ImageIdWithUrl imageIdWithUrl) {
        try {
            log.info("开始哈希图片 {}", imageIdWithUrl);
            //从七牛云下载图片
            String tempFilePath = tempDirectory + "/" + imageIdWithUrl.getImageId();
            qiniuUtils.download(imageIdWithUrl.getUrl(), tempFilePath);
            File tempFile = new File(tempFilePath);
            try {
                FileInputStream is = new FileInputStream(tempFilePath);
                //将图片哈希并存到数据库
                searchImageService.hashImageByDhashAlgorithm(is, imageIdWithUrl.getImageId());
                is.close();
            } catch (FileNotFoundException e) {
                log.error("系统错误 文件不存在{}", tempFilePath);
                return;
            } catch (IOException e) {
                log.error("文件流关闭错误 文件{}", tempFilePath);
                return;
            }
            tempFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("哈希图片消费者任务失败 error:{} 对象：{}", e.getLocalizedMessage(), imageIdWithUrl);
        } finally {
            String messageBody = new String(message.getBody());
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("哈希图片ack失败 error:{} message:{}", e.getMessage(), messageBody);
                try {
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                } catch (IOException ex) {
                    e.printStackTrace();
                    log.error("哈希图片reject失败 error:{} message:{}", e.getMessage(), messageBody);
                }
            }
        }
    }

    @RabbitListener(queues = HASH_IMAGE_QUEUE)
    @RabbitHandler
    public void hashTopicImagesFromQiniu(Channel channel, Message message, long topicId) {
        try {
            log.info("开始哈希图片 topicId:{}", topicId);
            List<Image> images = imageQueryService.listImagesOrderById(topicId);

            for (Image image : images) {
                //从七牛云下载图片
                String tempFilePath = tempDirectory + "/" + image.getId();
                qiniuUtils.download(image.getUrl(), tempFilePath);
                File tempFile = new File(tempFilePath);
                try {
                    FileInputStream is = new FileInputStream(tempFilePath);
                    //将图片哈希并存到数据库
                    searchImageService.hashImageByDhashAlgorithm(is, image.getId());
                    is.close();
                } catch (FileNotFoundException e) {
                    log.error("系统错误 文件不存在{}", tempFilePath);
                    return;
                } catch (IOException e) {
                    log.error("文件流关闭错误 文件{}", tempFilePath);
                    return;
                }
                tempFile.delete();
            }


        } catch (Exception e) {
            e.printStackTrace();
            log.error("哈希图片消费者任务失败 error:{} 话题：{}", e.getLocalizedMessage(), topicId);
        } finally {
            String messageBody = new String(message.getBody());
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("哈希图片ack失败 error:{} message:{}", e.getMessage(), messageBody);
                try {
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                } catch (IOException ex) {
                    e.printStackTrace();
                    log.error("哈希图片reject失败 error:{} message:{}", e.getMessage(), messageBody);
                }
            }
        }

    }


}

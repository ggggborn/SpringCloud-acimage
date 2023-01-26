package com.acimage.image.mq.consumer;


import cn.hutool.core.util.IdUtil;
import com.acimage.common.model.domain.Image;
import com.acimage.common.model.mq.dto.HashImagesUpdateDto;
import com.acimage.common.model.mq.dto.ImageIdWithUrl;
import com.acimage.common.utils.QiniuUtils;
import com.acimage.common.utils.common.ListUtils;
import com.acimage.common.utils.minio.MinioUtils;
import com.acimage.image.service.image.ImageMixWriteService;
import com.acimage.image.service.image.ImageQueryService;
import com.acimage.image.service.image.ImageWriteService;
import com.acimage.image.service.imagehash.SearchImageService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;


import javax.annotation.PostConstruct;
import java.io.*;
import java.util.List;

@Slf4j
@Configuration
@RabbitListener(queues = "hash-image-queue")
public class HashImageConsumer {
    private static final String HASH_IMAGE_QUEUE = "hash-image-queue";

    public String tempDirectory;
    @Autowired
    QiniuUtils qiniuUtils;
    @Autowired
    MinioUtils minioUtils;
    @Autowired
    SearchImageService searchImageService;
    @Autowired
    ImageQueryService imageQueryService;
    @Autowired
    ImageWriteService imageWriteService;
    @Autowired
    ImageMixWriteService imageMixWriteService;

    @PostConstruct
    public void init() {
        tempDirectory = System.getProperty("user.dir") + "//temp";
        //创建目录
        File directory = new File(tempDirectory);
        if (directory.mkdir()) {
            log.info("创建临时目录：{}", tempDirectory);
        }

    }

    @Deprecated
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

    @Deprecated
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


    @RabbitHandler
    public void hashTopicImagesFromMinio(Channel channel, Message message, HashImagesUpdateDto updateDto) {

        try {
            long topicId = updateDto.getTopicId();
            switch (updateDto.getServiceType()) {
                case ADD, UPDATE:
                    log.info("开始哈希图片 {}", updateDto);
                    List<String> addImageUrlList = updateDto.getAddImageUrls();
                    //获取实际存在的图片
                    List<Image> images = imageQueryService.listImagesForHavingNullTopicId(addImageUrlList);
                    for (Image image : images) {
                        //下载图片到本地
                        String tempFilePath = String.format("%s/%s", tempDirectory, image.getId() + IdUtil.fastSimpleUUID());
                        try {
                            minioUtils.download(image.getUrl(), tempFilePath);
                        } catch (Exception e) {
                            log.error("哈希图片时下载出错 error:{} url:{}", e.getLocalizedMessage(), image.getUrl());
                            continue;
                        }
                        File tempFile = new File(tempFilePath);
                        FileInputStream is = null;
                        try {
                            is = new FileInputStream(tempFilePath);
                            //将图片哈希并存到数据库
                            searchImageService.hashImageByDhashAlgorithm(is, image.getId());
                        } catch (FileNotFoundException e) {
                            log.error("系统错误 文件不存在{}", tempFilePath);
                            return;
                        } finally {
                            if (is != null) {
                                is.close();
                            }
                        }
                        tempFile.delete();
                    }
                    //更新图片对应话题id
                    List<Long> imageIds = ListUtils.extract(Image::getId, images);
                    imageWriteService.updateTopicIdForHavingNullTopicId(imageIds, topicId);

                    //删除图片
                    List<String> removeImageUrlList = updateDto.getAddImageUrls();
                    imageMixWriteService.removeTopicImages(topicId, removeImageUrlList);
                    break;
                    
                case DELETE:
                    imageMixWriteService.removeTopicImages(topicId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("哈希图片消费者任务失败 error:{} data：{}", e.getLocalizedMessage(), updateDto);
        } finally {
            String messageBody = new String(message.getBody());
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("哈希图片ack失败 error:{} message:{} data：{}", e.getMessage(), messageBody, updateDto);
                try {
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                } catch (IOException ex) {
                    e.printStackTrace();
                    log.error("哈希图片reject失败 error:{} message:{} data：{}", e.getMessage(), messageBody, updateDto);
                }
            }
        }

    }


}

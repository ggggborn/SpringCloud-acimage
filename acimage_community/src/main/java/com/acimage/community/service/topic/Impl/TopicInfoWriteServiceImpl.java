package com.acimage.community.service.topic.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.acimage.common.model.domain.Topic;
import com.acimage.common.result.Result;
import com.acimage.common.utils.HtmlUtils;
import com.acimage.common.utils.SensitiveWordUtils;
import com.acimage.common.utils.common.FileUtils;
import com.acimage.common.utils.minio.MinioUtils;
import com.acimage.community.global.consts.StorePrefixConst;
import com.acimage.community.listener.event.PublishTopicEvent;
import com.acimage.community.model.request.TopicAddReq;
import com.acimage.community.model.request.TopicAddReqBak2;
import com.acimage.community.mq.producer.HashImageMqProducer;
import com.acimage.community.mq.producer.RemoveTopicImagesMqProducer;
import com.acimage.community.service.comment.CommentWriteService;
import com.acimage.community.service.star.StarWriteService;
import com.acimage.community.service.topic.*;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.exception.BusinessException;
import com.acimage.common.utils.IdGenerator;
import com.acimage.community.service.userstatistic.UserCsWriteService;
import com.acimage.feign.client.ImageClient;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import java.util.*;


@Slf4j
@Service
public class TopicInfoWriteServiceImpl implements TopicInfoWriteService {

    @Autowired
    StarWriteService starWriteService;
    @Autowired
    CommentWriteService commentWriteService;
    @Autowired
    TopicQueryService topicQueryService;
    @Autowired
    TopicSpAttrWriteService topicSpAttrWriteService;
    @Autowired
    TopicWriteService topicWriteService;
    @Autowired
    ImageClient imageClient;
    @Autowired
    HashImageMqProducer hashImageMqProducer;
    @Autowired
    RemoveTopicImagesMqProducer removeTopicImagesMqProducer;
    @Autowired
    UserCsWriteService userCsWriteService;
    @Autowired
    TopicHtmlWriteService topicHtmlWriteService;
    @Autowired
    MinioUtils minioUtils;
    @Autowired
    SensitiveWordBs sensitiveWordBs;
    @Autowired
    SensitiveWordUtils sensitiveWordUtils;
    @Resource
    ApplicationContext applicationContext;



    @Override
    public long saveTopicAndImages(TopicAddReqBak2 topicAddReqBak2) {
        //生成id
        long topicId = IdGenerator.getSnowflakeNextId();
        String serviceToken = topicAddReqBak2.getServiceToken();

        //从image-service获取话题首张图url
        String firstImageUrl;
        Result<String> result = imageClient.updateTopicIdAndReturnFirstImageUrl(serviceToken, topicId);
        if (result.isOk()) {
            firstImageUrl = result.getData();
        } else {
            throw new BusinessException(result.getMsg());
        }

        Date now = new Date();
        //转化
        Topic topic = new Topic();
        BeanUtil.copyProperties(topicAddReqBak2, topic, false);
        topic.setId(topicId);
        topic.setCreateTime(now);
        topic.setUserId(UserContext.getUserId());
        topic.setCoverImageUrl(firstImageUrl);
        //插入topic
        topicWriteService.save(topic);

        //更新最新活跃时间
        topicSpAttrWriteService.changeActivityTime(topicId, now);

        //发送消息告诉image-service对图片哈希处理
        hashImageMqProducer.sendHashImagesMessage(topicId);

        PublishTopicEvent publishTopicEvent=new PublishTopicEvent(this,UserContext.getUserId(),topicId);
        applicationContext.publishEvent(publishTopicEvent);
        return topicId;
    }

    @Override
    public long saveTopicAndImagesBak(TopicAddReqBak2 topicAddReqBak2) {
        //生成id
        long topicId = IdGenerator.getSnowflakeNextId();
        String serviceToken = topicAddReqBak2.getServiceToken();

        //从image-service获取话题首张图url
        String firstImageUrl;
        Result<String> result = imageClient.updateTopicIdAndReturnFirstImageUrl(serviceToken, topicId);
        if (result.isOk()) {
            firstImageUrl = result.getData();
        } else {
            throw new BusinessException(result.getMsg());
        }

        Date now = new Date();
        //转化
        Topic topic = new Topic();
        BeanUtil.copyProperties(topicAddReqBak2, topic, false);
        topic.setId(topicId);
        topic.setCreateTime(now);
        topic.setUpdateTime(now);
        topic.setUserId(UserContext.getUserId());
        topic.setCoverImageUrl(firstImageUrl);
        //插入topic
        topicWriteService.save(topic);

        //更新最新活跃时间
        topicSpAttrWriteService.changeActivityTime(topicId, now);
        //发送消息告诉image-service对图片哈希处理
        hashImageMqProducer.sendHashImagesMessage(topicId);


        return topicId;
    }

    @Override
    public long saveTopicAndCoverImage(TopicAddReq topicAddReq, MultipartFile coverImage) {
        //生成id
        long topicId = IdGenerator.getSnowflakeNextId();
        Date now = new Date();
        String suffix=String.format("%s.%s",topicId, FileUtils.formatOf(coverImage));
        String url=minioUtils.generateUrl(StorePrefixConst.COVER_IMAGE,now,suffix);
        String coverImageUrl=minioUtils.upload(coverImage,url);
        //过滤标题
        String filterTile=SensitiveWordUtils.filter(topicAddReq.getTitle());
        //过滤内容
        String text= HtmlUtils.html2Text(topicAddReq.getHtml());
        String content= SensitiveWordUtils.filter(text);
        //提取前200个字作为文本内容
        String subContent= StrUtil.subPre(content,Topic.CONTENT_MAX);
        //转化
        Topic topic = new Topic();
        topic.setId(topicId);
        topic.setTitle(filterTile);
        topic.setContent(subContent);
        topic.setCreateTime(now);
        topic.setUpdateTime(now);
        topic.setActivityTime(now);
        topic.setUserId(UserContext.getUserId());
        topic.setCoverImageUrl(coverImageUrl);
        //保存topic
        topicWriteService.save(topic);
        //保存话题html
        topicHtmlWriteService.save(topicId,topicAddReq.getHtml());

        //更新最新活跃时间
        topicSpAttrWriteService.changeActivityTime(topicId, now);
//        //发送消息告诉image-service对图片哈希处理
//        hashImageMqProducer.sendHashImagesMessage(topicId);
        return topicId;
    }

    @Override
    public void removeTopicAndImages(long topicId) {
        Topic topic = topicQueryService.getTopic(topicId);
        if (topic == null) {
            log.error("话题不存在 话题:{} 用户:{}", topicId, UserContext.getUsername());
            throw new BusinessException("话题不存在~~");
        }

        if (!UserContext.getUserId().equals(topic.getUserId())) {
            log.error("异常删除：用户非话题主人 话题:{} 用户:{}", topicId, UserContext.getUsername());
            throw new BusinessException("非法操作！话题不属于你");
        }

        //删除star
        starWriteService.removeStars(topicId);
        //删除评论
        commentWriteService.removeComments(topicId);
        //删除话题
        topicHtmlWriteService.remove(topicId);
        topicWriteService.remove(topicId);

        //发送删除图片的消息
        removeTopicImagesMqProducer.sendRemoveTopicMessage(topicId);
        //删除相关属性
        topicSpAttrWriteService.removeAttributes(topicId);
        //更新用户统计数据
        userCsWriteService.updateTopicCountByIncrement(topic.getUserId(),-1);
    }


//    public void updateTopicAndImageDescriptions(TopicModifyReq topicModifyReq) throws BusinessException {
//        long topicId = topicModifyReq.getId();
//
//        String title = topicModifyReq.getTitle();
//        String content = topicModifyReq.getContent();
//        List<String> descriptions = topicModifyReq.getDescriptions();
//        //找到相关图片
//        List<Image> images = imageQueryService.listImagesOrderById(topicId);
//
//        if (images.size() == 0 || images.size() != topicModifyReq.getDescriptions().size()) {
//            log.error("用户：{} 修改 话题{} 数据异常：图片数与描述数不同", UserContext.getUsername(), topicId);
//            throw new BusinessException(Code.PARAM_INVALID, "数据异常：图片数与描述数不同");
//        }
//
//        List<Long> imageIds = ListUtils.extract(Image::getId, images);
//
//        imageWriteService.updateDescriptions(imageIds, descriptions);
//        topicWriteService.update(topicId, title, content);
//    }

}

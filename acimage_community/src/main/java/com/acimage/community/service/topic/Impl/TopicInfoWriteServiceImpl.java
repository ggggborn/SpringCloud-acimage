package com.acimage.community.service.topic.Impl;

import cn.hutool.core.util.StrUtil;
import com.acimage.common.global.consts.FileFormatConstants;
import com.acimage.common.global.enums.ServiceType;
import com.acimage.common.model.Index.TopicIndex;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.domain.community.TopicHtml;
import com.acimage.common.model.mq.dto.HashImagesUpdateDto;
import com.acimage.common.utils.*;
import com.acimage.common.utils.common.FileUtils;
import com.acimage.common.utils.common.ListUtils;
import com.acimage.common.utils.minio.MinioUtils;
import com.acimage.community.global.consts.CoverImageConstants;
import com.acimage.community.global.consts.StorePrefixConstants;
import com.acimage.community.listener.event.PublishTopicEvent;
import com.acimage.community.model.request.TopicAddReq;
import com.acimage.community.model.request.TopicModifyHtmlReq;
import com.acimage.community.mq.producer.HashImageMqProducer;
import com.acimage.community.mq.producer.RemoveTopicImagesMqProducer;
import com.acimage.community.mq.producer.SyncEsMqProducer;
import com.acimage.community.service.cmtyuser.CmtyUserWriteService;
import com.acimage.community.service.comment.CommentWriteService;
import com.acimage.community.service.star.StarWriteService;
import com.acimage.community.service.tag.TagQueryService;
import com.acimage.community.service.tag.TagTopicWriteService;
import com.acimage.community.service.topic.*;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import java.io.InputStream;
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
    TagQueryService tagQueryService;
    @Autowired
    HashImageMqProducer hashImageMqProducer;
    @Autowired
    SyncEsMqProducer syncEsMqProducer;
    @Autowired
    RemoveTopicImagesMqProducer removeTopicImagesMqProducer;
    @Autowired
    CmtyUserWriteService cmtyUserWriteService;
    @Autowired
    TopicHtmlWriteService topicHtmlWriteService;
    @Autowired
    TopicHtmlQueryService topicHtmlQueryService;
    @Autowired
    TagTopicWriteService tagTopicWriteService;
    @Autowired
    MinioUtils minioUtils;
    @Resource
    ApplicationContext applicationContext;


    @Override
    public long saveTopicInfo(TopicAddReq topicAddReq, MultipartFile coverImage) {
        //生成id
        long topicId = IdGenerator.getSnowflakeNextId();
        Date now = new Date();
        String suffix = String.format("%s.%s", topicId, FileUtils.formatOf(coverImage));
        String url = minioUtils.generateUrl(StorePrefixConstants.COVER_IMAGE, now, suffix);
        //图片压缩
        InputStream inputStream = ImageUtils.compressAsFixedWebpImage(coverImage,
                CoverImageConstants.WIDTH,
                CoverImageConstants.HEIGHT,
                CoverImageConstants.SIZE_AFTER_COMPRESS);

        String coverImageUrl = minioUtils.upload(inputStream, url, FileFormatConstants.WEBP_CONTENT_TYPE);
        //过滤标题
        String filterTile = SensitiveWordUtils.filter(topicAddReq.getTitle());
        //过滤内容
        String text = HtmlUtils.html2Text(topicAddReq.getHtml());
        String content = SensitiveWordUtils.filter(text);
        //提取前200个字作为文本内容
        String subContent = StrUtil.subPre(content, Topic.CONTENT_MAX);
        //转化
        Topic topic = Topic.builder()
                .id(topicId)
                .title(filterTile)
                .content(subContent)
                .coverImageUrl(coverImageUrl)
                .createTime(now)
                .updateTime(now)
                .activityTime(now)
                .userId(UserContext.getUserId())
                .categoryId(topicAddReq.getCategoryId())
                .build();

        //检查标签是否存在
        List<Integer> noRepeatTagIds = ListUtils.removeRepeat(Arrays.asList(topicAddReq.getTagIds()));
        tagQueryService.checkAndListTags(noRepeatTagIds);

        //保存topic
        topicWriteService.save(topic);
        //保存话题html
        topicHtmlWriteService.save(topicId, topicAddReq.getHtml());
        //保存标签
        tagTopicWriteService.save(topicId, noRepeatTagIds);

        //更新最新活跃时间
        topicSpAttrWriteService.changeActivityTime(topicId, now);
        //获取话题内的站内图片链接
        List<String> newImageUrlList = HtmlUtils.getInnerImageUrls(topicAddReq.getHtml());
        HashImagesUpdateDto updateDto = HashImagesUpdateDto.builder()
                .addImageUrls(newImageUrlList)
                .topicId(topicId)
                .serviceType(ServiceType.ADD)
                .build();

        //发送到mq，用于以图识图
        hashImageMqProducer.sendHashImagesMessage(updateDto);
        //同步到es
        TopicIndex topicIndex = TopicIndex.from(topic);
        topicIndex.setTagIds(noRepeatTagIds);
        //设置完整的content
        topicIndex.setContent(content);
        topicIndex.setStarCount(0);
        topicIndex.setCommentCount(0);
        topicIndex.setPageView(0);
        syncEsMqProducer.sendAddMessage(topicIndex);

        PublishTopicEvent publishTopicEvent = new PublishTopicEvent(this, UserContext.getUserId(), topicId);
        applicationContext.publishEvent(publishTopicEvent);
        return topicId;
    }

    @Override
    public void removeTopicInfo(long topicId) {
        Topic topic = topicQueryService.getTopic(topicId);
        if (topic == null) {
            log.error("话题不存在 话题:{} 用户:{}", topicId, UserContext.getUsername());
            throw new BusinessException("话题不存在~~");
        }

        if (!UserContext.getUserId().equals(topic.getUserId())) {
            log.error("非法删除：用户非话题主人 话题:{} 用户:{}", topicId, UserContext.getUsername());
            throw new BusinessException("非法操作！话题不属于你");
        }
        this.removeTopicInfoWithoutVerification(topicId);

//        //删除star
//        starWriteService.removeStars(topicId);
//        //删除评论
//        commentWriteService.removeComments(topicId);
//        //删除话题
//        topicHtmlWriteService.remove(topicId);
//        topicWriteService.remove(topicId);
//        //删除标签
//        tagTopicWriteService.remove(topicId);
//        //删除相关属性
//        topicSpAttrWriteService.removeAttributes(topicId);
//        //更新用户统计数据
//        cmtyUserWriteService.updateTopicCountByIncrement(userId, -1);
//        //发送删除图片的消息
//        removeTopicImagesMqProducer.sendRemoveTopicMessage(topicId);
//        //同步es数据
//        syncEsMqProducer.sendDeleteMessage(Long.toString(topicId), TopicIndex.class);
//        //同步到以图识图
//        HashImagesUpdateDto updateDto = HashImagesUpdateDto.builder()
//                .topicId(topicId)
//                .serviceType(ServiceType.DELETE)
//                .build();
//        hashImageMqProducer.sendHashImagesMessage(updateDto);
    }

    @Override
    public void removeTopicInfoWithoutVerification(long topicId){
        Topic topic = topicQueryService.getTopic(topicId);
        if (topic == null) {
            log.error("话题不存在 话题:{} 用户:{}", topicId, UserContext.getUsername());
            throw new BusinessException("话题不存在~~");
        }
        long userId=topic.getUserId();
        //删除star
        starWriteService.removeStars(topicId);
        //删除评论
        commentWriteService.removeComments(topicId);
        //删除话题
        topicHtmlWriteService.remove(topicId);
        topicWriteService.remove(topicId);
        //删除标签
        tagTopicWriteService.remove(topicId);
        //删除相关属性
        topicSpAttrWriteService.removeAttributes(topicId);
        //更新用户统计数据
        cmtyUserWriteService.updateTopicCountByIncrement(userId, -1);
        //发送删除图片的消息
        removeTopicImagesMqProducer.sendRemoveTopicMessage(topicId);
        //同步es数据
        syncEsMqProducer.sendDeleteMessage(Long.toString(topicId), TopicIndex.class);
        //同步到以图识图
        HashImagesUpdateDto updateDto = HashImagesUpdateDto.builder()
                .topicId(topicId)
                .serviceType(ServiceType.DELETE)
                .build();
        hashImageMqProducer.sendHashImagesMessage(updateDto);
    }

    @Override
    public void updateHtml(TopicModifyHtmlReq modifyReq) {
        long topicId = modifyReq.getId();
        Topic topic = topicQueryService.getTopic(topicId);
        if (topic == null) {
            log.error("话题不存在 话题:{} 用户:{}", topicId, UserContext.getUsername());
            throw new BusinessException("话题不存在~~");
        }

        if (!UserContext.getUserId().equals(topic.getUserId())) {
            log.error("异常修改：用户非话题主人 话题:{} 用户:{}", topicId, UserContext.getUsername());
            throw new BusinessException("非法操作！话题不属于你");
        }

        //过滤内容
        String text = HtmlUtils.html2Text(modifyReq.getHtml());
        String content = SensitiveWordUtils.filter(text);
        //提取前200个字作为文本内容
        String subContent = StrUtil.subPre(content, Topic.CONTENT_MAX);

        //获取变化的图片链接
        HashImagesUpdateDto updateDto = null;
        TopicHtml topicHtml = topicHtmlQueryService.getTopicHtml(topicId);
        if (topicHtml != null) {
            List<String> oldUrls = HtmlUtils.getInnerImageUrls(topicHtml.getHtml());
            List<String> newUrls = HtmlUtils.getInnerImageUrls(modifyReq.getHtml());
            List<String> addImageUrls = ListUtils.differenceSetOfV2(newUrls, oldUrls);
            List<String> removeImageUrls = ListUtils.differenceSetOfV2(oldUrls, newUrls);
            updateDto = HashImagesUpdateDto.builder()
                    .topicId(topicId)
                    .addImageUrls(addImageUrls)
                    .removeImageUrls(removeImageUrls)
                    .serviceType(ServiceType.UPDATE)
                    .build();
        }

        topicWriteService.updateContent(topicId, subContent);
        topicHtmlWriteService.update(topicId, SensitiveWordUtils.filter(modifyReq.getHtml()));

        TopicIndex topicIndex = TopicIndex.builder()
                .updateTime(new Date())
                .content(content)
                .id(topicId)
                .build();
        List<String> columns = LambdaUtils.columnsFrom(TopicIndex::getContent, TopicIndex::getUpdateTime);
        syncEsMqProducer.sendUpdateMessage(topicIndex, columns);

        //同步图片哈希
        if (updateDto != null) {
            hashImageMqProducer.sendHashImagesMessage(updateDto);
        }
    }

}

package com.acimage.community.service.topic.Impl;

import cn.hutool.core.util.StrUtil;
import com.acimage.common.global.consts.FileFormatConstants;
import com.acimage.common.global.enums.ServiceType;
import com.acimage.common.model.Index.TopicIndex;
import com.acimage.common.model.domain.community.CmtyUser;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.domain.community.TopicHtml;
import com.acimage.common.model.mq.dto.SyncImagesUpdateDto;
import com.acimage.common.utils.*;
import com.acimage.common.utils.common.ListUtils;
import com.acimage.common.utils.minio.MinioUtils;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.community.global.consts.CoverImageConstants;
import com.acimage.common.global.consts.StorePrefixConstants;
import com.acimage.community.global.consts.TopicKeyConstants;
import com.acimage.community.global.enums.TopicAttribute;
import com.acimage.community.listener.event.TopicEvent;
import com.acimage.community.model.request.TopicAddReq;
import com.acimage.community.model.request.TopicModifyHtmlReq;
import com.acimage.community.mq.producer.syncImagesMqProducer;
import com.acimage.community.mq.producer.SyncEsMqProducer;
import com.acimage.community.service.cmtyuser.CmtyUserWriteService;
import com.acimage.community.service.comment.CommentWriteService;
import com.acimage.community.service.star.StarWriteService;
import com.acimage.community.service.tag.TagQueryService;
import com.acimage.community.service.tag.TagTopicWriteService;
import com.acimage.community.service.topic.*;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.global.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;


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
    TopicRankWriteService topicRankWriteService;
    @Autowired
    TopicWriteService topicWriteService;
    @Autowired
    TagQueryService tagQueryService;
    @Autowired
    syncImagesMqProducer syncImagesMqProducer;
    @Autowired
    SyncEsMqProducer syncEsMqProducer;
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
    @Autowired
    RedisUtils redisUtils;
    @Resource
    ApplicationContext applicationContext;

    @Override
    public long saveTopicInfo(TopicAddReq topicAddReq, MultipartFile coverImage) {
        String publishedTitle = redisUtils.getForString(TopicKeyConstants.STRINGKP_PUBLISHED_TOPIC_TITLE);
        if (publishedTitle != null && publishedTitle.equals(topicAddReq.getTitle())) {
            log.warn("user:{}?????????????????? title:{}", UserContext.getUsername(), topicAddReq.getTitle());
            throw new BusinessException("???????????????????????????????????????");
        }
        //??????id
        long topicId = IdGenerator.getSnowflakeNextId();
        Date now = new Date();
        String suffix = String.format("%s.%s", topicId, FileFormatConstants.WEBP);
        String url = minioUtils.generateBaseUrl(StorePrefixConstants.COVER_IMAGE, now, suffix);
        //????????????
        InputStream inputStream = ImageUtils.compressAsFixedWebpImage(coverImage,
                CoverImageConstants.WIDTH,
                CoverImageConstants.HEIGHT,
                CoverImageConstants.LIMIT_COMPRESS_SIZE);

        String coverImageUrl = minioUtils.upload(inputStream, url, FileFormatConstants.WEBP_CONTENT_TYPE);
        //????????????
        String filterTile = SensitiveWordUtils.filter(topicAddReq.getTitle());
        //????????????
        String text = HtmlUtils.html2Text(topicAddReq.getHtml());
        String content = SensitiveWordUtils.filter(text);
        //?????????200????????????????????????
        String subContent = StrUtil.subPre(content, Topic.CONTENT_MAX);
        //??????
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
        if (UserContext.getUserId() != null) {
            CmtyUser user = new CmtyUser();
            user.setUsername(UserContext.getUsername());
            user.setPhotoUrl(UserContext.getPhotoUrl());
            user.setId(UserContext.getUserId());
            topic.setUser(user);
        }

        //????????????????????????
        List<Integer> noRepeatTagIds = ListUtils.removeRepeat(Arrays.asList(topicAddReq.getTagIds()));
        tagQueryService.checkAndListTags(noRepeatTagIds);

        //??????topic
        topicWriteService.save(topic);
        //????????????html
        topicHtmlWriteService.save(topicId, topicAddReq.getHtml());
        //????????????
        tagTopicWriteService.save(topicId, noRepeatTagIds);

        //?????????????????????
        topicRankWriteService.updateRank(TopicAttribute.ACTIVITY_TIME, topicId, now.getTime());
        //????????????????????????????????????
        List<String> newImageUrlList = HtmlUtils.getInnerImageUrls(topicAddReq.getHtml());
        SyncImagesUpdateDto updateDto = SyncImagesUpdateDto.builder()
                .addImageUrls(newImageUrlList)
                .topicId(topicId)
                .serviceType(ServiceType.ADD)
                .build();


        //?????????es
        TopicIndex topicIndex = TopicIndex.from(topic);
        topicIndex.setTagIds(noRepeatTagIds);
        //???????????????content
        topicIndex.setContent(content);
        topicIndex.setStarCount(0);
        topicIndex.setCommentCount(0);
        topicIndex.setPageView(0);

        syncEsMqProducer.sendAddMessage(topicIndex);
        //?????????mq?????????????????????
        syncImagesMqProducer.sendSyncImagesMessage(updateDto);

        TopicEvent topicEvent = new TopicEvent(this, UserContext.getUserId(), topicId);
        applicationContext.publishEvent(topicEvent);

        //???????????????????????????
        long timeout = 10L;
        redisUtils.setAsString(TopicKeyConstants.STRINGKP_PUBLISHED_TOPIC_TITLE + UserContext.getUserId(),
                topicAddReq.getTitle(), timeout, TimeUnit.SECONDS);

        return topicId;
    }

    @Override
    public void removeTopicInfo(long topicId) {
        Topic topic = topicQueryService.getTopic(topicId);
        if (topic == null) {
            log.error("??????????????? ??????:{} ??????:{}", topicId, UserContext.getUsername());
            throw new BusinessException("???????????????~~");
        }

        if (!UserContext.getUserId().equals(topic.getUserId())) {
            log.error("???????????????????????????????????? ??????:{} ??????:{}", topicId, UserContext.getUsername());
            throw new BusinessException("?????????????????????????????????");
        }
        this.removeTopicInfoWithoutVerification(topicId);

//        //??????star
//        starWriteService.removeStars(topicId);
//        //????????????
//        commentWriteService.removeComments(topicId);
//        //????????????
//        topicHtmlWriteService.remove(topicId);
//        topicWriteService.remove(topicId);
//        //????????????
//        tagTopicWriteService.remove(topicId);
//        //??????????????????
//        topicSpAttrWriteService.removeAttributes(topicId);
//        //????????????????????????
//        cmtyUserWriteService.updateTopicCountByIncrement(userId, -1);
//        //???????????????????????????
//        removeTopicImagesMqProducer.sendRemoveTopicMessage(topicId);
//        //??????es??????
//        syncEsMqProducer.sendDeleteMessage(Long.toString(topicId), TopicIndex.class);
//        //?????????????????????
//        SyncImagesUpdateDto updateDto = SyncImagesUpdateDto.builder()
//                .topicId(topicId)
//                .serviceType(ServiceType.DELETE)
//                .build();
//        syncImagesMqProducer.sendHashImagesMessage(updateDto);
    }

    @Override
    public void removeTopicInfoWithoutVerification(long topicId) {
        Topic topic = topicQueryService.getTopic(topicId);
        if (topic == null) {
            log.error("??????????????? ??????:{} ??????:{}", topicId, UserContext.getUsername());
            throw new BusinessException("???????????????~~");
        }
        long userId = topic.getUserId();
        //??????star
        starWriteService.removeStars(topicId);
        //????????????
        commentWriteService.removeComments(topicId);
        //????????????
        topicHtmlWriteService.remove(topicId);
        topicWriteService.remove(topicId);
        //????????????
        tagTopicWriteService.remove(topicId);
        //??????????????????
        topicSpAttrWriteService.removeAttributes(topicId);
        //????????????????????????
        cmtyUserWriteService.updateTopicCountByIncrement(userId, -1);

        //??????es??????
        syncEsMqProducer.sendDeleteMessage(Long.toString(topicId), TopicIndex.class);
        //????????????
        SyncImagesUpdateDto updateDto = SyncImagesUpdateDto.builder()
                .topicId(topicId)
                .serviceType(ServiceType.DELETE)
                .build();
        syncImagesMqProducer.sendSyncImagesMessage(updateDto);
    }

    @Override
    public void updateHtml(TopicModifyHtmlReq modifyReq) {
        long topicId = modifyReq.getId();
        Topic topic = topicQueryService.getTopic(topicId);
        if (topic == null) {
            log.error("??????????????? ??????:{} ??????:{}", topicId, UserContext.getUsername());
            throw new BusinessException("???????????????~~");
        }

        if (!UserContext.getUserId().equals(topic.getUserId())) {
            log.error("???????????????????????????????????? ??????:{} ??????:{}", topicId, UserContext.getUsername());
            throw new BusinessException("?????????????????????????????????");
        }

        //????????????
        String text = HtmlUtils.html2Text(modifyReq.getHtml());
        String content = SensitiveWordUtils.filter(text);
        //?????????200????????????????????????
        String subContent = StrUtil.subPre(content, Topic.CONTENT_MAX);

        //???????????????????????????
        SyncImagesUpdateDto updateDto = null;
        TopicHtml topicHtml = topicHtmlQueryService.getTopicHtml(topicId);
        if (topicHtml != null) {
            List<String> oldUrls = HtmlUtils.getInnerImageUrls(topicHtml.getHtml());
            List<String> newUrls = HtmlUtils.getInnerImageUrls(modifyReq.getHtml());
            List<String> addImageUrls = ListUtils.differenceSetOfV2(newUrls, oldUrls);
            List<String> removeImageUrls = ListUtils.differenceSetOfV2(oldUrls, newUrls);
            updateDto = SyncImagesUpdateDto.builder()
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

        //??????????????????
        if (updateDto != null) {
            syncImagesMqProducer.sendSyncImagesMessage(updateDto);
        }
    }

}

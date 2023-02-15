package com.acimage.image.service.image.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.model.domain.image.Image;
import com.acimage.common.utils.IdGenerator;
import com.acimage.common.deprecated.QiniuUtils;
import com.acimage.common.utils.minio.MinioUtils;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.common.utils.common.FileUtils;
import com.acimage.common.utils.common.PairUtils;
import com.acimage.image.dao.ImageDao;
import com.acimage.image.service.image.ImageQueryService;
import com.acimage.image.service.image.ImageWriteService;
import com.acimage.image.global.consts.TopicImageKeyConstants;
import com.acimage.image.service.imagehash.SearchImageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.util.annotation.Nullable;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ImageWriteServiceImpl extends ServiceImpl<ImageDao, Image> implements ImageWriteService {


    @Autowired
    ImageDao imageDao;
    @Autowired
    ImageQueryService imageQueryService;
    @Autowired(required = false)
    QiniuUtils qiniuUtils;
    @Autowired
    RedisUtils redisUtils;


    @Override
    public void saveImages(@Nullable List<Image> images) {
        if (images == null || images.size() == 0) {
            return;
        }
        imageDao.insertList(images);
    }

    @Override
    public Image saveImage(String url, int size, String fileName) {
        long imageId = IdGenerator.getSnowflakeNextId();
        Date now = new Date();
        Image image = Image.builder()
                .url(url)
                .id(imageId)
                .size(size)
                .fileName(fileName)
                .description(fileName)
                .createTime(now)
                .updateTime(now)
                .build();
        imageDao.insert(image);
        return image;
    }

    @Override
    public void removeImages(long topicId) {
        LambdaQueryWrapper<Image> qw = new LambdaQueryWrapper<>();
        qw.eq(Image::getTopicId, topicId);
        imageDao.delete(qw);
        //删除话题图片
        redisUtils.delete(TopicImageKeyConstants.STRINGKP_TOPIC_IMAGES);
    }

    @Override
    public int removeImages(long topicId, List<String> imageUrls) {
        LambdaQueryWrapper<Image> qw = new LambdaQueryWrapper<>();
        qw.in(Image::getUrl, imageUrls)
                .eq(Image::getTopicId, topicId);
        return imageDao.delete(qw);
    }

    @Override
    public void updateTopicId(List<Long> imageIds, long topicId) {
        if (CollectionUtil.isEmpty(imageIds)) {
            return;
        }
        LambdaUpdateWrapper<Image> uw = new LambdaUpdateWrapper<>();
        uw.in(Image::getId, imageIds)
                .set(Image::getTopicId, topicId);
        imageDao.update(null, uw);
    }

    @Override
    public void updateTopicIdForHavingNullTopicId(List<Long> imageIds, long topicId) {
        if (CollectionUtil.isEmpty(imageIds)) {
            return;
        }
        LambdaUpdateWrapper<Image> uw = new LambdaUpdateWrapper<>();
        uw.in(Image::getId, imageIds)
                .isNull(Image::getTopicId)
                .set(Image::getTopicId, topicId);
        imageDao.update(null, uw);
    }





}

package com.acimage.image.service.image.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.acimage.common.exception.BusinessException;
import com.acimage.common.model.domain.Image;
import com.acimage.common.utils.IdGenerator;
import com.acimage.common.utils.QiniuUtils;
import com.acimage.common.utils.RedisUtils;
import com.acimage.common.utils.common.FileUtils;
import com.acimage.common.utils.common.ListUtils;
import com.acimage.common.utils.minio.MinioUtils;
import com.acimage.image.dao.ImageDao;
import com.acimage.image.global.consts.StorePrefixConstants;
import com.acimage.image.service.image.ImageMixWriteService;
import com.acimage.image.service.image.ImageWriteService;
import com.acimage.image.service.image.consts.KeyConsts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ImageMixWriteServiceImpl implements ImageMixWriteService {
    @Autowired
    ImageWriteService imageWriteService;
    @Autowired
    QiniuUtils qiniuUtils;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    ImageDao imageDao;
    @Autowired
    MinioUtils minioUtils;

    @Override
    public String uploadImageFilesAndSaveImages(MultipartFile[] imageFiles) {
        //生成对象
        List<Image> imageList = imageWriteService.createImages(imageFiles, StorePrefixConstants.TOPIC_IMAGE);
        //上传
        for (int i = 0; i < imageFiles.length; i++) {
            qiniuUtils.upload(imageFiles[i], imageList.get(i).getUrl());
        }
        //保存
        imageWriteService.saveImages(imageList);
        //生成token
        String serviceToken = IdUtil.simpleUUID();
        //记录到redis
        List<Long> imageIds = ListUtils.extract(Image::getId, imageList);
        long expireMinutes = 19L;
        String key = KeyConsts.STRINGKP_PREPARED_TOPIC_IMAGES + serviceToken;
        redisUtils.setObjectJson(key, imageIds, expireMinutes, TimeUnit.MINUTES);

        return serviceToken;
    }

    @Override
    public List<Long> uploadAndSaveImages(MultipartFile[] imageFiles) {
        //生成对象
        List<Image> imageList = imageWriteService.createImages(imageFiles, StorePrefixConstants.TOPIC_IMAGE);
        //上传
        for (int i = 0; i < imageFiles.length; i++) {
            qiniuUtils.upload(imageFiles[i], imageList.get(i).getUrl());
        }
        //保存
        imageWriteService.saveImages(imageList);
        return ListUtils.extract(Image::getId, imageList);
    }

    @Override
    public String uploadAndSaveImage(MultipartFile imageFile) {
        long imageId= IdGenerator.getSnowflakeNextId();
        String suffix=String.format("%s.%s",imageId, FileUtils.formatOf(imageFile));
        String url=minioUtils.generateUrl(StorePrefixConstants.TOPIC_IMAGE,new Date(),suffix);
        //上传
        return minioUtils.upload(imageFile, url);
    }

    @Override
    public String updateTopicIdAndReturnFirstImageUrl(String serviceToken, long topicId) {
        //找到对应的imageIds
        String key = KeyConsts.STRINGKP_PREPARED_TOPIC_IMAGES + serviceToken;
        List<Long> imageIds = redisUtils.getListFromString(key, Long.class);
        if (CollectionUtil.isEmpty(imageIds)) {
            throw new BusinessException("该话题无图片");
        }
        //更新对应图片的topicId
        imageWriteService.updateTopicId(imageIds, topicId);

        Image firstImage = imageDao.selectById(imageIds.get(0));
        if (firstImage == null) {
            log.error("话题首个图片不存在 imageIds{}", imageIds);
            throw new BusinessException("该话题无图片");
        }

        return firstImage.getUrl();

    }
}

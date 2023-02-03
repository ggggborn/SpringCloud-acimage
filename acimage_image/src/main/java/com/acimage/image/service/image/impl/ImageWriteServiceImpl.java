package com.acimage.image.service.image.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.model.domain.image.Image;
import com.acimage.common.model.mq.dto.HashImagesUpdateDto;
import com.acimage.common.utils.IdGenerator;
import com.acimage.common.utils.QiniuUtils;
import com.acimage.common.utils.common.ListUtils;
import com.acimage.common.utils.minio.MinioUtils;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.common.utils.common.FileUtils;
import com.acimage.common.utils.common.PairUtils;
import com.acimage.image.dao.ImageDao;
import com.acimage.image.service.image.ImageQueryService;
import com.acimage.image.service.image.ImageWriteService;
import com.acimage.image.service.image.consts.KeyConsts;
import com.acimage.image.service.imagehash.SearchImageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.util.annotation.Nullable;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    @Autowired
    QiniuUtils qiniuUtils;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    MinioUtils minioUtils;
    @Autowired
    SearchImageService searchImageService;



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
        redisUtils.delete(KeyConsts.STRINGKP_TOPIC_IMAGES);
    }

    @Override
    public int removeImages(long topicId, List<String> imageUrls) {
        LambdaQueryWrapper<Image> qw = new LambdaQueryWrapper<>();
        qw.in(Image::getUrl, imageUrls)
                .eq(Image::getTopicId, topicId);
        return imageDao.delete(qw);
    }

    @Override
    public void updateDescription(long imageId, String description) {
        log.info("user:{} 修改图片描述 imageId:{} description:{}", UserContext.getUsername(), imageId, description);
        LambdaUpdateWrapper<Image> uw = new LambdaUpdateWrapper<>();
        uw.set(Image::getDescription, description).eq(Image::getId, imageId);
        imageDao.update(null, uw);

        //删除相应缓存
        Image image = imageQueryService.getImage(imageId);
        redisUtils.delete(KeyConsts.STRINGKP_IMAGE + imageId);
        redisUtils.delete(KeyConsts.STRINGKP_TOPIC_IMAGES + image.getTopicId());
    }

    @Override
    public void updateDescriptions(List<Long> imageIds, List<String> descriptions) {
        List<Pair<Long, String>> idAndDescriptions = PairUtils.combine(imageIds, descriptions);
        if (!CollectionUtil.isEmpty(idAndDescriptions)) {
            imageDao.updateDescription(idAndDescriptions);
        }

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


    @Override
    public List<Image> createImages(@NotNull MultipartFile[] imageFiles, String urlPrefix) {
        //根据MultipartFile创建image对象
        List<Image> images = new ArrayList<>();
        for (MultipartFile imageFile : imageFiles) {
            //取文件名前30字符作为文件初始描述
            String description = StrUtil.subPre(imageFile.getOriginalFilename(), Image.DESCRIPTION_MAX);
            long id = IdGenerator.getSnowflakeNextId();
            int size = (int) imageFile.getSize();
            Date now = new Date();
            String suffix = String.format("%s.%s", id, FileUtils.formatOf(imageFile.getOriginalFilename()));
            String url = qiniuUtils.generateUrl(suffix, now, urlPrefix);

            Image image = new Image(id, null, size, description);
            image.setUrl(url);

            images.add(image);
        }
        return images;
    }

    @Override
    public Image createImage(MultipartFile imageFile, String urlPrefix) {
        return null;
    }



}

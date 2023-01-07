package com.acimage.image.service.image.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.acimage.common.model.domain.Image;
import com.acimage.common.redis.annotation.QueryRedis;

import com.acimage.image.dao.ImageDao;
import com.acimage.image.service.image.ImageQueryService;
import com.acimage.image.service.image.consts.KeyConsts;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ImageQueryServiceImpl implements ImageQueryService {
    @Autowired
    ImageDao imageDao;

    @QueryRedis(keyPrefix = KeyConsts.STRINGKP_TOPIC_IMAGES, expire = 3L)
    @Override
    public List<Image> listImagesOrderById(long topicId) {
        LambdaQueryWrapper<Image> qw = new LambdaQueryWrapper<>();
        qw.orderByAsc(Image::getId).eq(Image::getTopicId, topicId);
        return imageDao.selectList(qw);
    }

    @QueryRedis(keyPrefix = KeyConsts.STRINGKP_IMAGE, expire = 3L)
    @Override
    public Image getImage(long imageId) {
        return imageDao.selectById(imageId);
    }

    @Override
    public List<Image> listImagesByImageIdsInOrder(List<Long> imageIds) {
        if(CollectionUtil.isEmpty(imageIds)){
            return new ArrayList<>();
        }

        LambdaQueryWrapper<Image> qw = new LambdaQueryWrapper<>();
        qw.in(Image::getId, imageIds);
        List<Image> images = imageDao.selectList(qw);

        //按照给出的imageIds顺序排好
        List<Image> orderedImages = new ArrayList<>();
        for (Long imageId : imageIds) {
            for (Image image : images) {
                if (imageId.equals(image.getId())) {
                    orderedImages.add(image);
                }
            }
        }

        return orderedImages;
    }
}

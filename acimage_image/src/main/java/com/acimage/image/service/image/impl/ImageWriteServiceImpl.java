package com.acimage.image.service.image.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.model.domain.Image;
import com.acimage.common.utils.IdGenerator;
import com.acimage.common.utils.QiniuUtils;
import com.acimage.common.utils.RedisUtils;
import com.acimage.common.utils.common.FileUtils;
import com.acimage.common.utils.common.PairUtils;
import com.acimage.image.dao.ImageDao;
import com.acimage.image.service.image.ImageQueryService;
import com.acimage.image.service.image.ImageWriteService;
import com.acimage.image.service.image.consts.KeyConsts;
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
    @Autowired
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
    public String saveImage(Image image) {
        return null;
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
    public void updateDescription(long imageId, String description) {
        log.info("user:{} 修改图片描述 imageId:{} description:{}", UserContext.getUsername(), imageId, description);
        LambdaUpdateWrapper<Image> uw = new LambdaUpdateWrapper<>();
        uw.set(Image::getDescription, description).eq(Image::getId, imageId);
        imageDao.update(null, uw);

        //删除相应缓存
        Image image=imageQueryService.getImage(imageId);
        redisUtils.delete(KeyConsts.STRINGKP_IMAGE+imageId);
        redisUtils.delete(KeyConsts.STRINGKP_TOPIC_IMAGES+image.getTopicId());
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

package com.acimage.image.service.image.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.acimage.common.exception.BusinessException;
import com.acimage.common.global.consts.FileFormat;
import com.acimage.common.model.domain.image.Image;
import com.acimage.common.model.mq.dto.HashImagesUpdateDto;
import com.acimage.common.utils.IdGenerator;
import com.acimage.common.utils.ImageUtils;
import com.acimage.common.utils.QiniuUtils;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.common.utils.common.ListUtils;
import com.acimage.common.utils.minio.MinioUtils;
import com.acimage.image.dao.ImageDao;
import com.acimage.image.global.consts.StorePrefixConstants;
import com.acimage.image.service.image.ImageMixWriteService;
import com.acimage.image.service.image.ImageQueryService;
import com.acimage.image.service.image.ImageWriteService;
import com.acimage.image.service.image.consts.KeyConsts;
import com.acimage.image.service.imagehash.ImageHashWriteService;
import com.acimage.image.service.imagehash.SearchImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ImageMixWriteServiceImpl implements ImageMixWriteService {

    public String tempDirectory;
    @PostConstruct
    public void init() {
        tempDirectory = System.getProperty("user.dir") + "//temp";
        //创建目录
        File directory = new File(tempDirectory);
        if (directory.mkdir()) {
            log.info("创建临时目录：{}", tempDirectory);
        }
    }
    @Autowired
    ImageWriteService imageWriteService;
    @Autowired
    ImageQueryService imageQueryService;
    @Autowired
    QiniuUtils qiniuUtils;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    ImageDao imageDao;
    @Autowired
    MinioUtils minioUtils;
    @Autowired
    ImageHashWriteService imageHashWriteService;
    @Autowired
    SearchImageService searchImageService;

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
        long imageId = IdGenerator.getSnowflakeNextId();
        String suffix = String.format("%s.%s", imageId, FileFormat.WEBP);
        String url = minioUtils.generateUrl(StorePrefixConstants.TOPIC_IMAGE, new Date(), suffix);
        //压缩为webp,压缩后不超过200kb
        int limitSize=200*1000;
        InputStream inputStream= ImageUtils.compressAsWebpImage(imageFile,limitSize);
        //上传
        String totalUrl = minioUtils.upload(inputStream, url,FileFormat.WEBP_CONTENT_TYPE);
        int size = (int) imageFile.getSize();
        //保存到数据库
        String fileName = imageFile.getOriginalFilename();
        imageWriteService.saveImage(totalUrl, size, fileName);
        return totalUrl;
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


    @Override
    public void removeTopicImages(long topicId, List<String> imageUrls) {
        if (CollectionUtil.isEmpty(imageUrls)) {
            return;
        }
        //找到要删除的图片id
        List<Long> imageIds = imageQueryService.listImageIds(topicId, imageUrls);
        //删除图片
        imageWriteService.removeImages(topicId, imageUrls);
        //删除图片哈希
        imageHashWriteService.removeImageHashes(imageIds);
    }

    @Override
    public void removeTopicImages(long topicId) {
        //找到要删除的图片id
        List<Long> imageIds = imageQueryService.listImageIds(topicId);
        //删除图片
        imageWriteService.removeImages(topicId);
        //删除图片哈希
        imageHashWriteService.removeImageHashes(imageIds);
    }


    @Override
    public void updateImageAndHash(HashImagesUpdateDto updateDto) {
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
                        minioUtils.downloadTo(image.getUrl(), tempFilePath);
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
                            try {
                                is.close();
                            } catch (IOException e) {
                                log.error("inputStream 关闭失败 {}",e.getMessage());
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    tempFile.delete();
                }
                //更新图片对应话题id
                List<Long> imageIds = ListUtils.extract(Image::getId, images);
                imageWriteService.updateTopicIdForHavingNullTopicId(imageIds, topicId);

                //删除图片
                List<String> removeImageUrlList = updateDto.getRemoveImageUrls();
                this.removeTopicImages(topicId, removeImageUrlList);
                break;

            case DELETE:
                this.removeTopicImages(topicId);
                break;
        }
    }
}

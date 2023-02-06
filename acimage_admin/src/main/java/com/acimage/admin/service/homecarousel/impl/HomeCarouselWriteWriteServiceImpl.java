package com.acimage.admin.service.homecarousel.impl;

import com.acimage.admin.dao.image.HomeCarouselDao;
import com.acimage.admin.service.homecarousel.HomeCarouselWriteService;
import com.acimage.common.global.consts.StorePrefixConstants;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.exception.BusinessException;
import com.acimage.common.model.domain.image.HomeCarousel;
import com.acimage.common.utils.common.FileUtils;
import com.acimage.common.utils.IdGenerator;
import com.acimage.common.utils.QiniuUtils;
import com.acimage.common.utils.minio.MinioUtils;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Slf4j
@Service
@DS("image")
public class HomeCarouselWriteWriteServiceImpl implements HomeCarouselWriteService {
    @Autowired
    HomeCarouselDao homeCarouselDao;
    @Autowired
    MinioUtils minioUtils;

    private final String URL_PREFIX = "homeCarousel";

    @Override
    public void saveHomeCarouselImage(MultipartFile multipartFile, String description) {
        long id = IdGenerator.getSnowflakeNextId();
        int size = (int) multipartFile.getSize();
        int location = homeCarouselDao.getMaxLocation() + 1;
        Date now = new Date();
        String fileName=String.format("%s.%s",id,FileUtils.formatOf(multipartFile));
        String url=minioUtils.generateUrl(StorePrefixConstants.HOME_CAROUSEL,now,fileName);
        String newUrl=minioUtils.upload(multipartFile, url);
        HomeCarousel homeCarousel = new HomeCarousel(id, description, newUrl,  location, size, now, now);
        homeCarouselDao.insert(homeCarousel);
    }

    @Override
    public void deleteHomeCarouselImage(long id) {
        HomeCarousel homeCarousel = homeCarouselDao.selectById(id);
        if (homeCarousel == null) {
            log.error("用户：{} 操作：删除主页走马灯 特殊图片id:{} 错误：图片不存在", UserContext.getUsername(), id);
            throw new BusinessException("该图片不存在");
        }

        LambdaUpdateWrapper<HomeCarousel> qw = new LambdaUpdateWrapper<>();
        qw.eq(HomeCarousel::getId, id);
        int col = homeCarouselDao.delete(qw);
        minioUtils.deleteFile(homeCarousel.getUrl());

    }

    @Override
    public void updateHomeCarouselImage(long id, String description) {
        LambdaUpdateWrapper<HomeCarousel> uw = new LambdaUpdateWrapper<>();
        uw.set(HomeCarousel::getDescription, description)
                .set(HomeCarousel::getUpdateTime, new Date())
                .eq(HomeCarousel::getId, id);


        int col = homeCarouselDao.update(null, uw);
        if (col == 0) {
            log.error("用户：{} 操作：修改主页走马灯描述 特殊图片id:{} 错误：图片非主页走马灯图片", UserContext.getUsername(), id);
            throw new BusinessException("该主页走马灯图片不存在");
        }
    }

    @Override
    public void coverHomeCarouselImage(long id, MultipartFile multipartFile) {
        HomeCarousel homeCarousel = homeCarouselDao.selectById(id);
        if (homeCarouselDao == null ) {
            log.error("用户：{} 操作：覆盖主页走马灯 特殊图片id:{} 错误：图片不存在", UserContext.getUsername(), id);
            throw new BusinessException("图片不存在");
        }

        int size = (int) multipartFile.getSize();
        Date now = new Date();
        String oldUrl = homeCarousel.getUrl();
        String suffix=String.format("%s.%s",IdGenerator.getSnowflakeNextId(), FileUtils.formatOf(multipartFile));
        String newUrl = minioUtils.generateUrl(suffix, now, URL_PREFIX);
        newUrl=minioUtils.upload(multipartFile,newUrl);
        LambdaUpdateWrapper<HomeCarousel> uw = new LambdaUpdateWrapper<>();
        uw.set(HomeCarousel::getSize, size)
                .set(HomeCarousel::getUpdateTime, now)
                .set(HomeCarousel::getUrl, newUrl)
                .eq(HomeCarousel::getId, id);
        homeCarouselDao.update(null, uw);

        //异步删除文件
        new Thread(() -> minioUtils.deleteFile(oldUrl)).start();

    }


}

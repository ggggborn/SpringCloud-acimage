package com.acimage.admin.service.impl;

import com.acimage.admin.dao.image.SpImageDao;
import com.acimage.admin.service.HomeCarouselService;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.global.enums.ImageType;
import com.acimage.common.exception.BusinessException;
import com.acimage.common.model.domain.SpImage;
import com.acimage.common.utils.common.FileUtils;
import com.acimage.common.utils.IdGenerator;
import com.acimage.common.utils.QiniuUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class HomeCarouselServiceImpl implements HomeCarouselService {
    @Autowired
    SpImageDao spImageDao;
    @Autowired
    QiniuUtils qiniuUtils;

    private final String URL_PREFIX = "homeCarousel";

    @Override
    public void saveHomeCarouselImage(MultipartFile multipartFile, String description) {
        long id = IdGenerator.getSnowflakeNextId();
        int size = (int) multipartFile.getSize();
        int location = spImageDao.getMaxLocation(ImageType.HOME_CAROUSEL) + 1;
        Date now = new Date();

        String url = qiniuUtils.generateUrl(Long.toString(id), now, URL_PREFIX);
        SpImage spImage = new SpImage(id, description, url, ImageType.HOME_CAROUSEL, location, size, now, now);
        spImageDao.insert(spImage);

        qiniuUtils.upload(multipartFile, url);
    }

    @Override
    public void deleteHomeCarouselImage(long id) {
        SpImage spImage = spImageDao.selectById(id);
        if (spImage == null || spImage.getType() != ImageType.HOME_CAROUSEL) {
            log.error("用户：{} 操作：删除主页走马灯 特殊图片id:{} 错误：图片不存在或非主页走马灯图片", UserContext.getUsername(), id);
            throw new BusinessException("该主页走马灯图片不存在");
        }

        LambdaUpdateWrapper<SpImage> qw = new LambdaUpdateWrapper<>();
        qw.eq(SpImage::getId, id).eq(SpImage::getType, ImageType.HOME_CAROUSEL);
        int col = spImageDao.delete(qw);

        if (col > 0) {
            //异步删除图片
            new Thread( () -> qiniuUtils.deleteFile(spImage.getUrl()) ).start();
        }
    }

    @Override
    public void updateHomeCarouselImage(long id, String description) {
        LambdaUpdateWrapper<SpImage> uw = new LambdaUpdateWrapper<>();
        uw.set(SpImage::getDescription, description)
                .set(SpImage::getUpdateTime, new Date())
                .eq(SpImage::getId, id)
                .eq(SpImage::getType, ImageType.HOME_CAROUSEL);

        int col = spImageDao.update(null, uw);
        if (col == 0) {
            log.error("用户：{} 操作：修改主页走马灯描述 特殊图片id:{} 错误：图片非主页走马灯图片", UserContext.getUsername(), id);
            throw new BusinessException("该主页走马灯图片不存在");
        }
    }

    @Override
    public void coverHomeCarouselImage(long id, MultipartFile multipartFile) {
        SpImage spImage = spImageDao.selectById(id);
        if (spImageDao == null || spImage.getType() != ImageType.HOME_CAROUSEL) {
            log.error("用户：{} 操作：覆盖主页走马灯 特殊图片id:{} 错误：图片不存在或非主页走马灯图片", UserContext.getUsername(), id);
            throw new BusinessException("图片不存在或非主页走马灯图片");
        }

        int size = (int) multipartFile.getSize();
        Date now = new Date();
        String oldUrl = spImage.getUrl();
        String suffix=String.format("%s.%s",IdGenerator.getSnowflakeNextId(), FileUtils.formatOf(multipartFile.getOriginalFilename()));
        String newUrl = qiniuUtils.generateUrl(suffix, now, URL_PREFIX);

        LambdaUpdateWrapper<SpImage> uw = new LambdaUpdateWrapper<>();
        uw.set(SpImage::getSize, size)
                .set(SpImage::getUpdateTime, now)
                .set(SpImage::getUrl, newUrl)
                .eq(SpImage::getId, id);
        spImageDao.update(null, uw);

        qiniuUtils.upload(multipartFile, newUrl);
        //异步删除文件
        new Thread(() -> qiniuUtils.deleteFile(oldUrl)).start();

    }

    @Override
    public List<SpImage> listHomeCarouselImages() {
        LambdaQueryWrapper<SpImage> qw = new LambdaQueryWrapper<>();
        qw.eq(SpImage::getType, ImageType.HOME_CAROUSEL);
        return spImageDao.selectList(qw);
    }
}

package com.acimage.community.test;

import com.acimage.common.model.domain.image.Image;
import com.acimage.common.utils.QiniuUtils;
import com.acimage.community.dao.ImageDao;
import com.acimage.community.global.consts.StorePrefixConst;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;

@Service
public class UploadService {

    @Autowired
    ImageDao imageDao;
    @Autowired
    QiniuUtils qiniuUtils;

    @Deprecated
    public void uploadLocalImagesToQiniu(){
        List<Image> allImages=imageDao.selectList(null);
        String basePath="D:\\Program\\nginx-1.22.0\\acimage_front\\storage\\images\\";
        for(Image image:allImages){
            String url=qiniuUtils.generateUrl(image.getId()+".jpeg",new Date(), StorePrefixConst.TOPIC_IMAGE);
            qiniuUtils.upload(new File(basePath+image.getId()+".jpeg"),url);

            LambdaUpdateWrapper<Image> uw=new LambdaUpdateWrapper<>();
            uw.eq(Image::getId,image.getId()).set(Image::getUrl,url);
            imageDao.update(null,uw);
        }
    }
}

package com.acimage.image.service.photo.impl;

import com.acimage.common.exception.BusinessException;
import com.acimage.common.global.consts.FileFormatConstants;
import com.acimage.common.result.Result;
import com.acimage.common.utils.IdGenerator;
import com.acimage.common.utils.ImageUtils;
import com.acimage.common.utils.QiniuUtils;
import com.acimage.common.utils.minio.MinioUtils;
import com.acimage.feign.client.UserClient;
import com.acimage.image.global.consts.StorePrefixConstants;
import com.acimage.image.service.photo.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;

@Service
public class PhotoServiceImpl implements PhotoService {
    @Autowired
    QiniuUtils qiniuUtils;
    @Autowired
    UserClient userClient;
    @Autowired
    MinioUtils minioUtils;

    @Override
    public String uploadPhotoAndUpdatePhotoUrl(MultipartFile photoFile){
        //上传头像到七牛云
        Date now = new Date();
        String suffix = String.format("%s.%s", IdGenerator.getSnowflakeNextId(), FileFormatConstants.WEBP);
        //压缩
        int limitSize=50*1000;
        int width=200;
        int height=200;
        InputStream inputStream= ImageUtils.compressAsFixedWebpImage(photoFile,width,height,limitSize);
        //上传
        String photoUrl = minioUtils.generateUrl(StorePrefixConstants.USER_PHOTO, now, suffix);
        minioUtils.upload(inputStream, photoUrl, FileFormatConstants.WEBP_CONTENT_TYPE);

        Result<String> result=userClient.modifyPhotoUrl(photoUrl);
        if(result.isOk()){
            return result.getData();
        }else{
            throw new BusinessException("头像修改失败");
        }

    }
}

package com.acimage.image.service.photo.impl;

import com.acimage.common.global.exception.BusinessException;
import com.acimage.common.global.consts.FileFormatConstants;
import com.acimage.common.result.Result;
import com.acimage.common.utils.IdGenerator;
import com.acimage.common.utils.ImageUtils;
import com.acimage.common.utils.minio.MinioUtils;
import com.acimage.feign.client.UserClient;
import com.acimage.common.global.consts.StorePrefixConstants;
import com.acimage.image.service.photo.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;

@Service
public class PhotoServiceImpl implements PhotoService {
    @Autowired
    UserClient userClient;
    @Autowired
    MinioUtils minioUtils;

    /**
     * 返回新token
     */
    @Override
    public String uploadPhotoAndUpdatePhotoUrl(MultipartFile photoFile){
        //上传头像到七牛云
        Date now = new Date();
        String suffix = String.format("%s.%s", IdGenerator.getSnowflakeNextId(), FileFormatConstants.WEBP);
        //压缩后限制大小
        int limitSize=20*1000;
        int width=200;
        int height=200;
        InputStream inputStream= ImageUtils.compressAsFixedWebpImage(photoFile,width,height,limitSize);
        //上传
        String photoUrl = minioUtils.generateBaseUrl(StorePrefixConstants.USER_PHOTO, now, suffix);
        photoUrl=minioUtils.upload(inputStream, photoUrl, FileFormatConstants.WEBP_CONTENT_TYPE);

        Result<String> result=userClient.modifyPhotoUrl(photoUrl);
        if(result.isOk()){
            return result.getData();
        }else{
            throw new BusinessException("头像修改失败");
        }

    }
}

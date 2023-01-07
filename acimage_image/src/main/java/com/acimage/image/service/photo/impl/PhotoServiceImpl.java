package com.acimage.image.service.photo.impl;

import com.acimage.common.exception.BusinessException;
import com.acimage.common.result.Result;
import com.acimage.common.utils.IdGenerator;
import com.acimage.common.utils.QiniuUtils;
import com.acimage.common.utils.common.FileUtils;
import com.acimage.feign.client.UserClient;
import com.acimage.image.global.consts.StorePrefixConstants;
import com.acimage.image.service.photo.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service
public class PhotoServiceImpl implements PhotoService {
    @Autowired
    QiniuUtils qiniuUtils;
    @Autowired
    UserClient userClient;

    @Override
    public String uploadPhotoAndUpdatePhotoUrl(MultipartFile photoFile){
        //上传头像到七牛云
        Date now = new Date();
        String format = FileUtils.formatOf(photoFile.getOriginalFilename());
        String suffix = String.format("%s.%s", IdGenerator.getSnowflakeNextId(), format);
        String photoUrl = qiniuUtils.generateUrl(suffix, now, StorePrefixConstants.USER_PHOTO);

        qiniuUtils.upload(photoFile, photoUrl);

        Result<String> result=userClient.modifyPhotoUrl(photoUrl);
        if(result.isOk()){
            return result.getData();
        }else{
            throw new BusinessException("头像修改失败");
        }

    }
}

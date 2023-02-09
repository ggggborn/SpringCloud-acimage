package com.acimage.image.web.bak;


import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.result.Result;
import com.acimage.common.deprecated.annotation.Authentication;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.global.exception.BusinessException;
import com.acimage.image.service.FileService;
import com.acimage.image.service.imagehash.ImageHashWriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@Validated
@Authentication
@RequestMapping("/api/files/hashImages")
public class FileProvider {
    @Autowired
    FileService fileService;
    @Autowired
    ImageHashWriteService imageHashWriteService;

    @DeleteMapping
    public Result deleteImageHashes(@RequestParam("imageIds") List<Long> imageIds) {
        if(imageIds==null||imageIds.size()> Topic.IMAGES_AMOUNT_MAX||imageIds.size()< Topic.IMAGES_AMOUNT_MIN){
            throw new BusinessException("图片数量异常");
        }
        imageHashWriteService.removeImageHashes(imageIds);
        return Result.ok();
    }

    /**
     * 废弃原因：图片不再存储在本地，存在七牛云
     * @param imageFiles
     * @param imageIds
     * @return
     */
    @Deprecated
    @PostMapping("/imageFiles")
    public Result storeImageFiles(@RequestParam("imageFiles") MultipartFile[] imageFiles,@RequestParam("imageIds") List<Long> imageIds) {
        if(imageIds==null||imageFiles==null
                ||imageIds.size()> Topic.IMAGES_AMOUNT_MAX
                ||imageIds.size()< Topic.IMAGES_AMOUNT_MIN
                ||imageFiles.length!=imageIds.size()){
            log.error("用户：{} 存储图片 错误：图片数量不符合规范", UserContext.getUsername());
            throw new BusinessException("图片数量异常");
        }
        fileService.storeImageFiles(imageFiles,imageIds);
        return Result.ok();
    }

    @PostMapping("/uploadPhoto")
    public Result storePhotoFiles(@RequestPart("photoFile") MultipartFile photoFile){
        fileService.storePhotoFiles(photoFile);
        return Result.ok();
    }

}

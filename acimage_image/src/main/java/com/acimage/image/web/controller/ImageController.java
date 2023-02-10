package com.acimage.image.web.controller;


import com.acimage.common.deprecated.annotation.Authentication;
import com.acimage.common.global.consts.FileFormatConstants;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.model.domain.image.Image;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.result.Result;
import com.acimage.common.utils.common.FileUtils;
import com.acimage.image.service.image.ImageMixWriteService;
import com.acimage.image.service.image.ImageWriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;


@RestController
@Slf4j
@Validated
@Authentication
@RequestMapping("/api/image/images")
public class ImageController {
    @Autowired
    ImageMixWriteService imageMixWriteService;
    @Autowired
    ImageWriteService imageWriteService;


    @PostMapping("/upload/topicImage")
    public Result<String> uploadTopicImage(@RequestParam("imageFile") MultipartFile imageFile) {

        String originName = imageFile.getOriginalFilename();

        String format = FileUtils.formatOf(originName);
        if (!FileFormatConstants.ALLOWED_IMAGE_FORMAT.contains(format)) {
            return Result.fail("图片格式需为"+FileFormatConstants.ALLOWED_IMAGE_FORMAT);
        }
        log.info("用户：{} 话题: 上传话题图片", UserContext.getUsername());
        return Result.ok(imageMixWriteService.saveImage(imageFile));
    }

}

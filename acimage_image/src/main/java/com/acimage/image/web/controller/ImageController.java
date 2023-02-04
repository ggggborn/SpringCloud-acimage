package com.acimage.image.web.controller;


import com.acimage.common.global.annotation.Authentication;
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

    @PostMapping("/upload/topicImages")
    public Result<String> uploadTopicImages(@RequestParam("imageFiles") MultipartFile[] imageFiles) {
        //校验图片数量
        if (imageFiles == null
                || imageFiles.length < Topic.IMAGES_AMOUNT_MIN
                || imageFiles.length > Topic.IMAGES_AMOUNT_MAX) {
            log.warn("user：{} 发表话题 error：话题图片数量不符", UserContext.getUsername());
            return Result.fail(Topic.IMAGE_VALIDATION_MSG);
        }

        //校验图片格式以及文件名长度
        for (MultipartFile imageFile : imageFiles) {
            String originName = imageFile.getOriginalFilename();
            if (originName == null || originName.length() < Image.DESCRIPTION_MIN) {
                return Result.fail("图片文件名长度小于2");
            }
            String format = FileUtils.formatOf(originName);
            if (!FileFormatConstants.ALLOWED_IMAGE_FORMAT.contains(format)) {
                return Result.fail("图片格式需为jpg，jpeg，png");
            }
        }

        log.info("用户：{} 话题: 上传文件数量：{}", UserContext.getUsername(), imageFiles.length);
        String serviceToken = imageMixWriteService.uploadImageFilesAndSaveImages(imageFiles);
//        List<Long> imageIds=imageMixWriteService.uploadAndSaveImages(imageFiles);
        return Result.ok(serviceToken);
    }

    @PostMapping("/upload/topicImage")
    public Result<String> uploadTopicImage(@RequestParam("imageFile") MultipartFile imageFile) {

        String originName = imageFile.getOriginalFilename();
        if(originName.length()<Image.DESCRIPTION_MIN||originName.length()>Image.FILE_NAME_MAX){
            return Result.fail(Image.FILE_NAME_VALIDATION_MSG);
        }
        String format = FileUtils.formatOf(originName);
        if (!FileFormatConstants.ALLOWED_IMAGE_FORMAT.contains(format)) {
            return Result.fail("图片格式需为jpg，jpeg，png");
        }
        log.info("用户：{} 话题: 上传话题图片", UserContext.getUsername());
        return Result.ok(imageMixWriteService.uploadAndSaveImage(imageFile));
    }

    @PutMapping("/description/{id}/{description}")
    public Result<?> modifyDescription(@PathVariable Long id, @PathVariable @Size(min = Image.DESCRIPTION_MIN, max = Image.DESCRIPTION_MAX,
            message = Image.DESCRIPTION_VALIDATION_MSG) String description) {
        imageWriteService.updateDescription(id, description);
        return Result.ok();
    }


}

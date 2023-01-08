package com.acimage.admin.web.controller;


import cn.hutool.core.util.StrUtil;
import com.acimage.admin.service.HomeCarouselService;
import com.acimage.common.global.consts.FileFormat;
import com.acimage.common.model.domain.SpImage;
import com.acimage.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@RestController
@Slf4j
@RequestMapping("/api/admin/SpImages/homeCarousel")
public class HomeCarouselController {
    @Autowired
    HomeCarouselService homeCarouselService;

    @PostMapping
    public Result addImage(@RequestParam("image") MultipartFile imageFile,
                           @RequestParam("description") @Size(min = SpImage.DESC_MIN, max = SpImage.DESC_MAX, message = SpImage.DESC_INVALID_MSG) String description) {
        String originName=imageFile.getOriginalFilename();
        String format = StrUtil.subAfter(originName, '.', true);
        if (!FileFormat.ALLOWED_IMAGE_FORMAT.contains(format)) {
            return Result.fail("图片格式需为jpg，jpeg，png");
        }
        homeCarouselService.saveHomeCarouselImage(imageFile, description);
        return Result.ok();
    }


    @DeleteMapping
    public Result deleteImage(@RequestParam("id") @Positive Long id) {
        homeCarouselService.deleteHomeCarouselImage(id);
        return Result.ok();
    }

    @PutMapping("/description")
    public Result modifyDescription(@RequestParam("id") @Positive Long id,
                                    @RequestParam("description") @Size(min = SpImage.DESC_MIN, max = SpImage.DESC_MAX, message = SpImage.DESC_INVALID_MSG) String description) {
        homeCarouselService.updateHomeCarouselImage(id, description);
        return Result.ok();
    }

    @PostMapping("/cover")
    public Result coverImageFile(@RequestParam("id") @Positive Long id,
                                 @RequestParam("image") MultipartFile imageFile) {
        String originName=imageFile.getOriginalFilename();
        String format = StrUtil.subAfter(originName, '.', true);
        if (!FileFormat.ALLOWED_IMAGE_FORMAT.contains(format)) {
            return Result.fail("图片格式需为jpg，jpeg，png");
        }
        homeCarouselService.coverHomeCarouselImage(id, imageFile);
        return Result.ok();
    }

    @GetMapping("/current")
    public Result queryCurrentHomeCarousel(){
        return Result.ok(homeCarouselService.listHomeCarouselImages());

    }

}

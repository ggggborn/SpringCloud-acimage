package com.acimage.admin.web.controller;


import cn.hutool.core.util.StrUtil;
import com.acimage.admin.model.request.CarouselAddReq;
import com.acimage.admin.model.request.CarouselModifyReq;
import com.acimage.admin.service.homecarousel.HomeCarouselWriteService;
import com.acimage.admin.service.homecarousel.HomeCarouselQueryService;
import com.acimage.common.global.consts.FileFormatConstants;
import com.acimage.common.model.domain.community.HomeCarousel;
import com.acimage.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/admin/homeCarousels")
@Validated
public class HomeCarouselController {
    @Autowired
    HomeCarouselWriteService homeCarouselWriteService;
    @Autowired
    HomeCarouselQueryService homeCarouselQueryService;

    @PostMapping
    public Result<?> addImage(@RequestParam("image") MultipartFile imageFile,
                              @Validated @ModelAttribute CarouselAddReq carouselAddReq) {
        String originName = imageFile.getOriginalFilename();
        String format = StrUtil.subAfter(originName, '.', true);
        if (!FileFormatConstants.ALLOWED_CAROUSEL_FORMAT.contains(format)) {
            return Result.fail("图片格式需为"+FileFormatConstants.ALLOWED_CAROUSEL_FORMAT);
        }
        homeCarouselWriteService.saveHomeCarouselImage(imageFile, carouselAddReq);
        return Result.ok();
    }


    @DeleteMapping("/{id}")
    public Result<?> deleteImage(@PathVariable @Positive Long id) {
        homeCarouselWriteService.deleteHomeCarouselImage(id);
        return Result.ok();
    }

    @PutMapping("/descriptionAndLink")
    public Result<?> modifyDescription(@Validated @ModelAttribute CarouselModifyReq carouselModifyReq) {
        homeCarouselWriteService.updateHomeCarouselImage(carouselModifyReq);
        return Result.ok();
    }

    @PostMapping("/cover")
    public Result<?> coverImageFile(@RequestParam("id") @Positive Long id,
                                    @RequestParam("image") MultipartFile imageFile) {
        String originName = imageFile.getOriginalFilename();
        String format = StrUtil.subAfter(originName, '.', true);
        if (!FileFormatConstants.ALLOWED_CAROUSEL_FORMAT.contains(format)) {
            return Result.fail("图片格式需为"+FileFormatConstants.ALLOWED_CAROUSEL_FORMAT);
        }
        homeCarouselWriteService.coverHomeCarouselImage(id, imageFile);
        return Result.ok();
    }

    @GetMapping("/current")
    public Result<List<HomeCarousel>> queryCurrentHomeCarousel() {
        return Result.ok(homeCarouselQueryService.listCurrent());

    }

}

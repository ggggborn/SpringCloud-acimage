package com.acimage.admin.web.controller;


import cn.hutool.core.util.StrUtil;
import com.acimage.admin.service.homecarousel.HomeCarouselWriteService;
import com.acimage.admin.service.homecarousel.HomeCarouselQueryService;
import com.acimage.common.global.consts.FileFormat;
import com.acimage.common.model.domain.HomeCarousel;
import com.acimage.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/admin/homeCarousels")
public class HomeCarouselController {
    @Autowired
    HomeCarouselWriteService homeCarouselWriteService;
    @Autowired
    HomeCarouselQueryService homeCarouselQueryService;

    @PostMapping
    public Result<?> addImage(@RequestParam("image") MultipartFile imageFile,
                              @RequestParam("description") @Size(min = HomeCarousel.DESC_MIN, max = HomeCarousel.DESC_MAX, message = HomeCarousel.DESC_INVALID_MSG) String description) {
        String originName = imageFile.getOriginalFilename();
        String format = StrUtil.subAfter(originName, '.', true);
        if (!FileFormat.ALLOWED_IMAGE_FORMAT.contains(format)) {
            return Result.fail("图片格式需为jpg，jpeg，png");
        }
        homeCarouselWriteService.saveHomeCarouselImage(imageFile, description);
        return Result.ok();
    }


    @DeleteMapping("/{id}")
    public Result<?> deleteImage(@PathVariable @Positive Long id) {
        homeCarouselWriteService.deleteHomeCarouselImage(id);
        return Result.ok();
    }

    @PutMapping("/description")
    public Result<?> modifyDescription(@RequestParam("id") @Positive Long id,
                                       @RequestParam("description") @Size(min = HomeCarousel.DESC_MIN, max = HomeCarousel.DESC_MAX, message = HomeCarousel.DESC_INVALID_MSG) String description) {
        homeCarouselWriteService.updateHomeCarouselImage(id, description);
        return Result.ok();
    }

    @PostMapping("/cover")
    public Result<?> coverImageFile(@RequestParam("id") @Positive Long id,
                                    @RequestParam("image") MultipartFile imageFile) {
        String originName = imageFile.getOriginalFilename();
        String format = StrUtil.subAfter(originName, '.', true);
        if (!FileFormat.ALLOWED_IMAGE_FORMAT.contains(format)) {
            return Result.fail("图片格式需为jpg，jpeg，png");
        }
        homeCarouselWriteService.coverHomeCarouselImage(id, imageFile);
        return Result.ok();
    }

    @GetMapping("/current")
    public Result<List<HomeCarousel>> queryCurrentHomeCarousel() {
        return Result.ok(homeCarouselQueryService.listCurrent());

    }

}

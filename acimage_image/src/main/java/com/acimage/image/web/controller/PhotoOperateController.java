package com.acimage.image.web.controller;


import com.acimage.common.deprecated.annotation.Authentication;
import com.acimage.common.result.Result;
import com.acimage.image.service.photo.impl.PhotoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@Slf4j
@Validated
@Authentication
@RequestMapping("/api/image/photos/operate")
public class PhotoOperateController {
    @Autowired
    PhotoServiceImpl photoService;

    @PostMapping("/upload")
    public Result<String> uploadPhoto(@RequestParam("photoFile") MultipartFile photoFile) {
        String url = photoService.uploadPhotoAndUpdatePhotoUrl(photoFile);
        return Result.ok(url);
    }


}

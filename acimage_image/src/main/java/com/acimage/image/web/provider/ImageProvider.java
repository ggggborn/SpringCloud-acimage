package com.acimage.image.web.provider;


import com.acimage.common.deprecated.annotation.Authentication;
import com.acimage.common.global.enums.AuthenticationType;
import com.acimage.common.model.domain.image.Image;
import com.acimage.common.result.Result;
import com.acimage.image.service.image.ImageMixWriteService;
import com.acimage.image.service.image.ImageQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@Validated
@RequestMapping("/image/images")
public class ImageProvider {
    @Autowired
    ImageMixWriteService imageMixWriteService;
    @Autowired
    ImageQueryService imageQueryService;

    @GetMapping("/topicId/{topicId}")
    public Result<List<Image>> queryTopicImages(@PathVariable Long topicId) {
        return Result.ok(imageQueryService.listImagesOrderById(topicId));
    }

}

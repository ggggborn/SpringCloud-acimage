package com.acimage.image.web.bak;


import com.acimage.common.global.annotation.Authentication;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.exception.BusinessException;
import com.acimage.common.model.domain.Topic;
import com.acimage.image.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Controller
@Slf4j
@Validated
@Authentication
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    FileService fileService;


    @PostMapping("/download/imageFiles")
    public void downloadImageFiles(@RequestParam("imageIds") List<@Positive @NotNull Long> imageIds, HttpServletResponse resp) {
        if(imageIds==null||imageIds.size()> Topic.IMAGES_AMOUNT_MAX||imageIds.size()< Topic.IMAGES_AMOUNT_MIN){
            log.error("用户：{} 下载图片 错误：数量异常", UserContext.getUsername());
            throw new BusinessException("图片数量异常");
        }
        fileService.downloadImageFiles(imageIds,resp);
    }





}

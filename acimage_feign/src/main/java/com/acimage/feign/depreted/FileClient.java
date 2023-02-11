package com.acimage.feign.depreted;

import com.acimage.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(value="image-service/api/image/hashImages")
public interface FileClient {
    @GetMapping("sayHello")
    String sayHello();

    @DeleteMapping
    Result deleteImageHashes(@RequestParam("imageIds") List<Long> imageIds);

    /**
     * 废弃原因：图片不再存储在本地，存储在七牛云
     * @param imageFiles
     * @param imageIds
     * @return
     */
    @Deprecated
    @PostMapping(value ="/imageFiles",produces = {MediaType.APPLICATION_JSON_VALUE},consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result storeImageFiles(@RequestPart("imageFiles") MultipartFile[] imageFiles, @RequestParam("imageIds") List<Long> imageIds);

    @PostMapping(value = "/uploadPhoto",produces = {MediaType.APPLICATION_JSON_VALUE},consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result storePhotoFiles(@RequestPart("photoFile") MultipartFile photoFile);
}

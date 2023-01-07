package com.acimage.image.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

public interface FileService {

    void storeImageFiles(MultipartFile[] imageFiles,List<Long> imageIds);

    void storePhotoFiles(MultipartFile photoFile);

    void removeImageFiles(List<Long> imageIds);

    void downloadImageFiles(List<@Positive @NotNull Long> imageIds, HttpServletResponse resp);

}

package com.acimage.image.service.photo;

import org.springframework.web.multipart.MultipartFile;

public interface PhotoService {
    String uploadPhotoAndUpdatePhotoUrl(MultipartFile photoFile);
}

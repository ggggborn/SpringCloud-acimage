package com.acimage.image.service.image;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageMixWriteService {
    @Deprecated
    @Transactional(rollbackFor = Exception.class)
    String uploadImageFilesAndSaveImages(MultipartFile[] imageFiles);

    @Transactional(rollbackFor = Exception.class)
    List<Long> uploadAndSaveImages(MultipartFile[] imageFiles);

    String uploadAndSaveImage(MultipartFile imageFile);

    @Transactional(rollbackFor = Exception.class)
    String updateTopicIdAndReturnFirstImageUrl(String serviceToken, long topicId);

    void removeTopicImages(long topicId, List<String> imageUrls);

    void removeTopicImages(long topicId);
}

package com.acimage.image.service.image;

import com.acimage.common.model.mq.dto.SyncImagesUpdateDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageMixWriteService {
    @Deprecated
    @Transactional(rollbackFor = Exception.class)
    String uploadImageFilesAndSaveImages(MultipartFile[] imageFiles);

    @Transactional(rollbackFor = Exception.class)
    List<Long> uploadAndSaveImages(MultipartFile[] imageFiles);

    String saveImage(MultipartFile imageFile);

    @Transactional(rollbackFor = Exception.class)
    String updateTopicIdAndReturnFirstImageUrl(String serviceToken, long topicId);

    void removeTopicPartialImages(long topicId, List<String> imageUrls);

    void removeTopicPartialImages(long topicId);

    void updateImageAndHash(SyncImagesUpdateDto updateDto);
}

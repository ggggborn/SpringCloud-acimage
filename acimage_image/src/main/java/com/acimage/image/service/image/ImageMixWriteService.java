package com.acimage.image.service.image;

import com.acimage.common.model.mq.dto.SyncImagesUpdateDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageMixWriteService {


    String saveImage(MultipartFile imageFile);

    void removeTopicPartialImages(long topicId, List<String> imageUrls);

    void removeTopicImages(long topicId);

    void updateImageAndHash(SyncImagesUpdateDto updateDto);
}

package com.acimage.image.service.image;

import com.acimage.common.model.domain.image.Image;

import java.util.List;

public interface ImageQueryService {
    List<Image> listImagesOrderById(long topicId);

    List<Image> listImagesByIds(List<Long> imageIds);

    List<Long> listImageIds(long topicId, List<String> imageUrls);

    List<Long> listImageIds(long topicId);

    List<Image> listImagesForHavingNullTopicId(List<String> imageUrls);
}

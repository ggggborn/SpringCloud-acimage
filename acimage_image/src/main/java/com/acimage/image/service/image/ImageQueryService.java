package com.acimage.image.service.image;

import com.acimage.common.model.domain.image.Image;
import com.acimage.common.redis.annotation.QueryRedis;
import com.acimage.image.service.image.consts.KeyConsts;

import java.util.List;

public interface ImageQueryService {
    List<Image> listImagesOrderById(long topicId);

    @QueryRedis(keyPrefix = KeyConsts.STRINGKP_IMAGE, expire = 3L)
    Image getImage(long imageId);

    List<Image> listImagesByImageIdsInOrder(List<Long> imageIds);

    List<Long> listImageIds(long topicId, List<String> imageUrls);

    List<Long> listImageIds(long topicId);

    List<Image> listImagesForHavingNullTopicId(List<String> imageUrls);
}

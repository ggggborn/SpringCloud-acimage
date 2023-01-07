package com.acimage.admin.service;

import com.acimage.common.model.domain.SpImage;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HomeCarouselService {
    @Transactional(rollbackFor = Exception.class,transactionManager = "imageTransactionManager")
    void saveHomeCarouselImage(MultipartFile multipartFile, String description);
    void deleteHomeCarouselImage(long id);
    void updateHomeCarouselImage(long id, String description);
    @Transactional(rollbackFor = Exception.class,transactionManager = "imageTransactionManager")
    void coverHomeCarouselImage(long id, MultipartFile multipartFile);

    List<SpImage> listHomeCarouselImages();
}

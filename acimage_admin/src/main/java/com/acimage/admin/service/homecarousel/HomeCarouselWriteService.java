package com.acimage.admin.service.homecarousel;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface HomeCarouselWriteService {
    @Transactional(rollbackFor = Exception.class)
    void saveHomeCarouselImage(MultipartFile multipartFile, String description);
    void deleteHomeCarouselImage(long id);
    void updateHomeCarouselImage(long id, String description);
    @Transactional(rollbackFor = Exception.class)
    void coverHomeCarouselImage(long id, MultipartFile multipartFile);


}

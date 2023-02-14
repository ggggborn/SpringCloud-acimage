package com.acimage.admin.service.homecarousel;

import com.acimage.admin.model.request.CarouselAddReq;
import com.acimage.admin.model.request.CarouselModifyReq;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface HomeCarouselWriteService {
    @Transactional(rollbackFor = Exception.class)
    void saveHomeCarouselImage(MultipartFile multipartFile, CarouselAddReq carouselAddReq);
    void deleteHomeCarouselImage(long id);
    void updateHomeCarouselImage(CarouselModifyReq modifyReq);
    @Transactional(rollbackFor = Exception.class)
    void coverHomeCarouselImage(long id, MultipartFile multipartFile);


}

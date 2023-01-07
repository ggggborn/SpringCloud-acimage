package com.acimage.image.service.image;


import com.acimage.common.model.domain.Image;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ImageWriteService extends IService<Image> {

    void saveImages(List<Image> images);
    void removeImages(long topicId);

    void updateDescription(long imageId, String description);

    @Transactional
    void updateDescriptions(List<Long> imageIds,List<String> descriptions);

    void updateTopicId(List<Long> imageIds,long topicId);

    List<Image> createImages(MultipartFile[] imageFiles, String urlPrefix);


}

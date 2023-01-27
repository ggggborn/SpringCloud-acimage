package com.acimage.image.service.image;


import com.acimage.common.model.domain.image.Image;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ImageWriteService extends IService<Image> {

    @Deprecated
    void saveImages(List<Image> images);
    Image saveImage(String url,int size,String fileName);
    void removeImages(long topicId);

    int removeImages(long topicId, List<String> imageUrls);

    @Deprecated
    void updateDescription(long imageId, String description);
    @Transactional
    void updateDescriptions(List<Long> imageIds,List<String> descriptions);
    void updateTopicId(List<Long> imageIds,long topicId);

    void updateTopicIdForHavingNullTopicId(List<Long> imageIds, long topicId);

    @Deprecated
    List<Image> createImages(MultipartFile[] imageFiles, String urlPrefix);
    Image createImage(MultipartFile imageFile,String urlPrefix);


}

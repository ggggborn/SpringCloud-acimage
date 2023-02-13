package com.acimage.image.service.imagehash;

import com.acimage.common.model.domain.image.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface SearchImageService {
    @Deprecated
    void processImagesHashForNotProcessedImages();

    void hashImageByDhashAlgorithm(InputStream imageInputStream, long imageId) ;

    List<Image> searchMostSimilarImages(MultipartFile imageFile);

    void removeImageHashes(List<Long> imageIds);

}

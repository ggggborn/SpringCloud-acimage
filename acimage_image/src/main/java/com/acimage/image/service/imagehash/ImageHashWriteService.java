package com.acimage.image.service.imagehash;

import java.io.InputStream;
import java.util.List;

public interface ImageHashWriteService {

    void removeImageHashes(List<Long> imageIds);

    void HashImagesByDhash(InputStream imageInputStream, long imageId) ;
}

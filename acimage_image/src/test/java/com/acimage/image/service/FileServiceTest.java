package com.acimage.image.service;



import com.acimage.image.service.imagehash.SearchImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SpringBootTest
public class FileServiceTest {



    @Autowired
    SearchImageService searchImageService;

    @Test
    void searchMostSimilarImagesTest(){
        File file=new File("F:\\MyImage\\素材\\爱丽丝.jpeg");
        file=new File("F:\\MyImage\\素材\\大忍.jpg");
        MultipartFile cMultiFile = null;
        try {
            cMultiFile = new MockMultipartFile("file", file.getName(), null, new FileInputStream(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(searchImageService.searchMostSimilarImages(cMultiFile));
    }

}

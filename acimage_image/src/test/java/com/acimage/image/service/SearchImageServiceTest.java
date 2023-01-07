package com.acimage.image.service;



import com.acimage.image.service.imagehash.SearchImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
public class SearchImageServiceTest {

    @Autowired
    SearchImageService searchImageService;

    @Test
    void processImagesHashForNotProcessedImagesTest(){
        InputStream inputStream=null;
        try {
            inputStream = new FileInputStream("F:\\MyImage\\素材\\0001.jpeg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        searchImageService.hashImageByDhashAlgorithm(inputStream,1L);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}

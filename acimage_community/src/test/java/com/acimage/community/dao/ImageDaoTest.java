package com.acimage.community.dao;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class ImageDaoTest {
    @Autowired
    ImageDao imageDao;

    @Test
    public void selectImagesWithTopic(){
        List<Long> imageIds= Arrays.asList(1572508721903943680L,1572508721903943681L);
        System.out.println(imageDao.selectImagesWithTopic(imageIds));

    }
}

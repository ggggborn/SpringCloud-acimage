package com.acimage.admin.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HomeCarouselServiceTest {

    @Autowired
    HomeCarouselService homeCarouselService;

    @Test
    void updateImageTest(){
        long id=1595422558785183744L;
        String description="三分钟diy虚拟形象改";
        homeCarouselService.updateHomeCarouselImage(id,description);
    }
}

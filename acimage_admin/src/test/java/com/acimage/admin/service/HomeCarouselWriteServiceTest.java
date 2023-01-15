package com.acimage.admin.service;

import com.acimage.admin.service.homecarousel.HomeCarouselWriteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HomeCarouselWriteServiceTest {

    @Autowired
    HomeCarouselWriteService homeCarouselWriteService;

    @Test
    void updateImageTest(){
        long id=1595422558785183744L;
        String description="三分钟diy虚拟形象改";
        homeCarouselWriteService.updateHomeCarouselImage(id,description);
    }
}

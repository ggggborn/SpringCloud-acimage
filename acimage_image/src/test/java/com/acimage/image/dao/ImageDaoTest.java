package com.acimage.image.dao;


import com.acimage.common.model.domain.image.Image;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
public class ImageDaoTest {
    @Autowired
    ImageDao imageDao;

    @Test
    public void testSelectAll() {
        long topicId = 1585529145054986240L;
        List<Image> imageList = imageDao.selectListOrderById(topicId);
        System.out.println(imageList);
        System.out.println(imageList.size());
    }


}

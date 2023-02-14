package com.acimage.admin.dao.community;


import com.acimage.common.model.domain.community.HomeCarousel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class SpDaoTest {
    @Autowired
    HomeCarouselDao homeCarouselDao;

    @Test
    public void deleteTest(){
        long id=10086L;
        homeCarouselDao.deleteById(id);
    }

    @Test
    public void getMaxLocationOfHomeCarousel(){
        System.out.println(homeCarouselDao.getMaxLocation());
    }

    @Test
    public void testSelectAll(){
        System.out.println(homeCarouselDao.selectList(null));

        System.out.println(homeCarouselDao.count());
    }

}

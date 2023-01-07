package com.acimage.admin.dao.community;


import com.acimage.admin.dao.image.SpImageDao;
import com.acimage.common.global.enums.ImageType;
import com.acimage.common.model.domain.SpImage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class SpDaoTest {
    @Autowired
    SpImageDao spImageDao;
    @Test
    public void insertTest(){
        SpImage spImage=new SpImage(10086L,"10086","/test/888", ImageType.HOME_CAROUSEL,1,10086,new Date());
        spImageDao.insert(spImage);
        System.out.println(spImageDao.selectOne(null));
    }

    @Test
    public void deleteTest(){
        long id=10086L;
        spImageDao.deleteById(id);
    }

    @Test
    public void getMaxLocationOfHomeCarousel(){
        System.out.println(spImageDao.getMaxLocation(ImageType.HOME_CAROUSEL));
    }

    @Test
    public void testSelectAll(){
        System.out.println(spImageDao.selectList(null));

        System.out.println(spImageDao.count());
    }

}

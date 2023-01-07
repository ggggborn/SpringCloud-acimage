package com.acimage.image.dao;




import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class ImageHashDaoDaoTest {
    @Autowired
    ImageHashDao imageHashDao;

    @Test
    public void testSelectAll(){
        System.out.println(imageHashDao.selectList(null));
    }



}

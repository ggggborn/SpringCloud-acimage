package com.acimage.admin.dao.community;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TopicDaoTest {

    @Autowired
    TopicDao topicDao;

    @Test
    void testSelectAll(){
        System.out.println(topicDao.selectList(null));

    }
}

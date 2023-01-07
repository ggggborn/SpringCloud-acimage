package com.acimage.community.dao;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
public class StarDaoTest {
    @Autowired
    StarDao starDao;


    @Test
    public void countStarsByUserId(){

        long userId=1572443275490078720L;
        System.out.println(starDao.countStarsOwnedBy(userId));



    }

    @Test
    public void countSelectStarsWithTopic(){

        long userId=1572443275490078720L;
        System.out.println(starDao.selectStarsWithTopicOrderByCreateTime(userId,1,5));

    }


}

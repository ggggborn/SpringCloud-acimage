package com.acimage.community.dao;


import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.utils.LambdaUtils;
import com.acimage.community.service.topic.TopicInfoWriteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TopicDaoTest {
    @Autowired
    TopicDao topicDao;

    @Autowired
    TopicInfoWriteService topicInfoWriteService;

    @Test
    public void selectTopicWithUserImagesComments() {
        long id = 1572508721685839872L;

    }

    @Test
    public void selectTopicsOrderByScan() {
        String startTime = "2022-09-23 00:00:00";
        List<Topic> topics = topicDao.selectTopicsWithUserImagesOrderByPageView(startTime, null);
        System.out.println(topics);
    }

    @Test
    public void getTopicCount() {
        long userId = 1572443275490078720L;
    }

    @Test
    public void testSelectTopicCount() {
        long userId = 0;

        System.out.println(topicDao.countTopics(userId));
    }

    @Test
    public void testSelectTopicsWithUserOrderBy() {
        String column = LambdaUtils.getUnderlineColumnName(Topic::getPageView);
        List<Topic> topicList = topicDao.selectTopicsWithUserOrderBy(column, 100);
        System.out.println(topicList);
        System.out.println(topicList.size());

    }


}

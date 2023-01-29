package com.acimage.community.service;

import com.acimage.common.model.domain.community.Comment;
import com.acimage.community.service.comment.CommentInfoService;
import com.acimage.community.service.topic.TopicSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TopicSearchServiceTest {
    @Autowired
    TopicSearchService topicSearchService;
    @Test
    public void testSelectList(){
        System.out.println(topicSearchService.searchByTagId(21,1,10));
    }
}


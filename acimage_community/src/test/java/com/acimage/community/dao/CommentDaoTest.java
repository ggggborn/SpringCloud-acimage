package com.acimage.community.dao;


import com.acimage.common.model.domain.community.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CommentDaoTest {
    @Autowired
    CommentDao commentDao;
    @Test
    public void testSelectList(){
        long topicId=1572508721685839872L;
        List<Comment> commentList=commentDao.selectCommentsWithUser(topicId,1,5);
        for(Comment comment:commentList){
            System.out.println(comment);
        }
        System.out.println(commentList.size());
    }

    @Test
    public void testCountComments(){
        long topicId=1572508721685839872L;
        System.out.println(commentDao.countCommentsByTopicId(topicId));
    }

    @Test
    public void testSelectCommentsWithTopicOrderByCreateTime(){
        long userId=1572443275490078720L;
        System.out.println(commentDao.selectCommentsWithTopicOrderByCreateTime(userId,0,10));
    }



}

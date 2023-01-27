package com.acimage.community.service;

import com.acimage.common.model.domain.community.Comment;
import com.acimage.community.service.comment.CommentInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CommentWriteServiceTest {
    @Autowired
    CommentInfoService commentIInfoService;
    @Test
    public void testSelectList(){
        long topicId=1572508721685839872L;
        List<Comment> commentList=commentIInfoService.pageCommentsWithUser(topicId,1);
        for(Comment comment:commentList){
            System.out.println(comment);
        }
        System.out.println(commentList.size());
    }
}


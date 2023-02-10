package com.acimage.community.service;

import com.acimage.common.model.domain.community.Comment;
import com.acimage.community.service.comment.CommentInfoQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CommentWriteServiceTest {
    @Autowired
    CommentInfoQueryService commentIInfoService;

}


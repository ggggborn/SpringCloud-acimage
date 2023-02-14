package com.acimage.admin.web.controller;


import com.acimage.admin.model.request.CommentQueryReq;
import com.acimage.admin.model.request.TopicQueryReq;
import com.acimage.admin.service.comment.CommentQueryService;
import com.acimage.admin.service.topic.TopicQueryService;
import com.acimage.common.model.domain.community.Comment;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.page.MyPage;
import com.acimage.common.result.Result;
import com.acimage.common.utils.LambdaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/admin/comments/query")
@Validated
public class CommentQueryController {
    @Autowired
    CommentQueryService commentQueryService;

    @GetMapping("/by")
    public Result<MyPage<Comment>> pageCommentsBy(@Validated @ModelAttribute CommentQueryReq queryReq) {

        return Result.ok(commentQueryService.pageCommentsBy(queryReq));
    }
}

package com.acimage.community.web.controller;


import com.acimage.common.result.Result;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.model.domain.community.Comment;
import com.acimage.community.global.consts.PageSizeConstants;
import com.acimage.community.service.comment.CommentInfoQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequestMapping("/api/community/comments/query")
public class CommentQueryController {
    @Autowired
    CommentInfoQueryService commentInfoQueryService;

    @GetMapping("/topicComments")
    public Result<?> pageTopicComments(@Positive @RequestParam("topicId") Long topicId,
                                       @Positive @RequestParam("pageNo") Integer pageNo) {
        List<Comment> comments = commentInfoQueryService.pageCommentsWithUser(topicId, pageNo, PageSizeConstants.TOPIC_COMMENTS);
        return Result.ok(comments);
    }

    @GetMapping("/mine/{pageNo}")
    public Result<?> pageMyComments(@Positive @NotNull @PathVariable("pageNo") Integer pageNo) {
        return Result.ok(commentInfoQueryService.pageCommentsWithTopicOrderByCreateTime(UserContext.getUserId(),
                pageNo,
                PageSizeConstants.ACTIVITY_COMMENTS));
    }
}

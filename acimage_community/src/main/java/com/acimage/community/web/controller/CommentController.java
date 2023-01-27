package com.acimage.community.web.controller;


import com.acimage.common.result.Code;
import com.acimage.common.result.Result;
import com.acimage.common.global.annotation.Authentication;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.model.domain.community.Comment;
import com.acimage.community.model.request.CommentAddReq;
import com.acimage.community.model.request.CommentModifyReq;
import com.acimage.community.service.comment.CommentInfoService;
import com.acimage.community.service.comment.CommentWriteService;
import com.acimage.community.service.topic.TopicInfoWriteService;
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
@RequestMapping("/api/community/comments")
@Authentication
public class CommentController {
    @Autowired
    CommentWriteService commentWriteService;
    @Autowired
    CommentInfoService commentInfoService;
    @Autowired
    TopicInfoWriteService topicRankService;

    @PostMapping
    public Result addComment(@Validated @RequestBody CommentAddReq commentAddReq){
        commentWriteService.saveComment(commentAddReq);
        log.info("评论了 {}", commentAddReq);
        return new Result(Code.OK,null,"评论成功");
    }

    @PutMapping
    public Result modifyComment(@Validated @RequestBody CommentModifyReq commentModifyReq){
        log.info("修改了 {}", commentModifyReq);
        commentWriteService.updateComment(commentModifyReq);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result deleteComment(@Positive @NotNull @PathVariable("id") Long id){
        log.info("删除 评论{}",id);
        commentWriteService.removeComment(id);
        return Result.ok();
    }

    @GetMapping("/pageComments")
    public Result pageComments(@Positive @RequestParam("topicId") Long topicId,
                        @Positive @RequestParam("pageNo") Integer pageNo){
        List<Comment> comments= commentInfoService.pageCommentsWithUser(topicId,pageNo);
        return Result.ok(comments);
    }

    @GetMapping("/mine/{pageNo}")
    public Result queryCommentsOfCurrentUser(@Positive @NotNull @PathVariable("pageNo") Integer pageNo){
        return Result.ok(commentInfoService.pageCommentsWithTopicOrderByCreateTime(UserContext.getUserId(),pageNo));
    }
}

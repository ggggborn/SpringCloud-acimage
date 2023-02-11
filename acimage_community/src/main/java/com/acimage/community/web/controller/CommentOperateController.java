package com.acimage.community.web.controller;



import com.acimage.common.result.Result;
import com.acimage.community.model.request.CommentAddReq;
import com.acimage.community.model.request.CommentModifyReq;
import com.acimage.community.service.comment.CommentWriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@Slf4j
@Validated
@RequestMapping("/api/community/comments/operate")
public class CommentOperateController {
    @Autowired
    CommentWriteService commentWriteService;

    @PostMapping
    public Result<?> addComment(@Validated @RequestBody CommentAddReq commentAddReq){
        if(commentAddReq.getContent().trim().length()<2){
            return Result.fail("评论有效字数不能少于2个字");
        }
        commentWriteService.saveComment(commentAddReq);
        log.info("评论了 {}", commentAddReq);
        return Result.ok("评论成功");
    }

    @PutMapping
    public Result<?> modifyComment(@Validated @RequestBody CommentModifyReq commentModifyReq){
        log.info("修改了 {}", commentModifyReq);
        commentWriteService.updateComment(commentModifyReq);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteComment(@Positive @NotNull @PathVariable("id") Long id){
        log.info("删除 评论{}",id);
        commentWriteService.removeComment(id);
        return Result.ok();
    }

}

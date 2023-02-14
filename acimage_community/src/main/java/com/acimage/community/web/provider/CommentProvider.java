package com.acimage.community.web.provider;

import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.redis.annotation.RequestLimit;
import com.acimage.common.redis.enums.LimitTarget;
import com.acimage.common.result.Result;
import com.acimage.community.service.comment.CommentWriteService;
import com.acimage.community.service.topic.TopicInfoWriteService;
import com.acimage.community.service.topic.TopicQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/community/comments")
@Validated
public class CommentProvider {

    @Autowired
    CommentWriteService commentWriteService;


    @RequestLimit(limitTimes = {1},durations = {3},penaltyTimes = {1},targets = {LimitTarget.USER})
    @DeleteMapping("/{id}")
    public Result<?> deleteComment(@Positive @NotNull @PathVariable("id") Long id){
        log.info("删除 评论{}",id);
        commentWriteService.removeCommentWithoutVerification(id);
        return Result.ok();
    }

}

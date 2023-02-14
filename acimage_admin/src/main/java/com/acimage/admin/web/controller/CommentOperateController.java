package com.acimage.admin.web.controller;


import com.acimage.admin.model.request.CommentQueryReq;
import com.acimage.admin.service.comment.CommentQueryService;
import com.acimage.admin.service.comment.CommentWriteService;
import com.acimage.common.model.domain.community.Comment;
import com.acimage.common.model.page.MyPage;
import com.acimage.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@RestController
@Slf4j
@RequestMapping("/api/admin/comments/operate")
@Validated
public class CommentOperateController {
    @Autowired
    CommentWriteService commentWriteService;

    @DeleteMapping("/{id}")
    public Result<?> pageCommentsBy(@PathVariable @Positive Long id) {
        commentWriteService.delete(id);
        return Result.ok();
    }
}

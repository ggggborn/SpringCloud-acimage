package com.acimage.user.web.controller;


import com.acimage.common.global.context.UserContext;
import com.acimage.common.model.domain.user.UserMsg;
import com.acimage.common.result.Result;
import com.acimage.user.service.commentmsg.CommentMsgQueryService;
import com.acimage.user.service.commentmsg.CommentMsgWriteService;
import com.acimage.user.service.usermsg.UserMsgWriteService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user/messages/query")
@Validated
public class MessageController {
    @Autowired
    CommentMsgQueryService commentMsgQueryService;
    @Autowired
    UserMsgWriteService userMsgWriteService;

    @GetMapping("/comments/page/{pageNo}/{pageSize}")
    public Result<?> pageCommentMessages(@Range(min = 1, max = 100, message = "页码在1-100之间") @PathVariable("pageNo") Integer pageNo,
                                         @Range(min = 5, max = 10, message = "页大小在5-10之间") @PathVariable("pageSize") Integer pageSize) {
        userMsgWriteService.resetMsgCount(UserContext.getUserId(), UserMsg::getCommentMsgCount);
        return Result.ok(commentMsgQueryService.pageMyCommentMsg(UserContext.getUserId(),
                pageNo, pageSize));
    }
}

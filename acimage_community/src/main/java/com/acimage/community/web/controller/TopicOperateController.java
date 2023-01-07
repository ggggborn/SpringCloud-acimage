package com.acimage.community.web.controller;


import com.acimage.common.global.annotation.Authentication;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.model.domain.Topic;
import com.acimage.common.result.Result;
import com.acimage.community.model.request.TopicAddReq;
import com.acimage.community.model.request.TopicModifyContentReq;
import com.acimage.community.service.topic.TopicInfoWriteService;
import com.acimage.community.service.topic.TopicWriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;


@RestController
@Slf4j
@RequestMapping("/api/community/topics")
@Validated
@Authentication
public class TopicOperateController {

    @Autowired
    TopicInfoWriteService topicInfoWriteService;
    @Autowired
    TopicWriteService topicWriteService;

    @PostMapping
    public Result<Long> addTopic(@Validated @RequestBody TopicAddReq topicAddReq) {
        //校验图片数量
        log.info("user：{} 发表话题 参数:{}", UserContext.getUsername(), topicAddReq);
        long topicId = topicInfoWriteService.saveTopicAndImages(topicAddReq);
        return Result.ok(topicId);
    }

    @PutMapping("/title/{id}/{title}")
    public Result<?> modifyTitle(@Positive @PathVariable Long id,
                                 @PathVariable @Size(min = Topic.TITLE_MIN, max = Topic.TITLE_MAX, message = Topic.TITLE_VALIDATION_MSG)  String title) {
        topicWriteService.updateTitle(id,title);
        return Result.ok();
    }

    @PutMapping("/content")
    public Result<?> modifyContent(@Validated @RequestBody TopicModifyContentReq topicModifyContentReq) {
        topicWriteService.updateContent(topicModifyContentReq);
        return Result.ok();
    }


    @DeleteMapping("/{id}")
    public Result<?> deleteTopic(@Positive @PathVariable("id") Long id) {
        log.info("删除了话题：{}", id);
        topicInfoWriteService.removeTopicAndImages(id);
        return Result.ok();
    }


}

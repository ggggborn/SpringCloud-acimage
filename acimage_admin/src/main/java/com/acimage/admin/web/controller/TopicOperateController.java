package com.acimage.admin.web.controller;


import com.acimage.admin.service.topic.TopicWriteService;
import com.acimage.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;

@RestController
@Slf4j
@RequestMapping("/api/admin/topics/operate")
@Validated
public class TopicOperateController {
    @Autowired
    TopicWriteService topicWriteService;

    @DeleteMapping("/{topicId}")
    public Result<?> delete(@PathVariable @Positive Long topicId){
        topicWriteService.remove(topicId);
        return Result.ok();
    }
}

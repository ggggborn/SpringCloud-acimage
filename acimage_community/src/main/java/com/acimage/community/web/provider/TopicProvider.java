package com.acimage.community.web.provider;

import com.acimage.common.global.annotation.Authentication;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.result.Result;
import com.acimage.community.service.topic.TopicQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/community/topics")
@Validated
@Authentication
public class TopicProvider {

    @Autowired
    TopicQueryService topicQueryService;

    @GetMapping("/ids")
    public Result<List<Topic>> queryTopics(@RequestParam("topicIds") List<Long> topicIds){
        return Result.ok(topicQueryService.listTopics(topicIds));
    }

}

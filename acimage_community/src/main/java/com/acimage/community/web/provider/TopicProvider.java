package com.acimage.community.web.provider;

import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.result.Result;
import com.acimage.community.service.topic.TopicInfoWriteService;
import com.acimage.community.service.topic.TopicQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/community/topics")
@Validated
public class TopicProvider {

    @Autowired
    TopicQueryService topicQueryService;
    @Autowired
    TopicInfoWriteService topicInfoWriteService;

    @GetMapping("/ids")
    public Result<List<Topic>> queryTopics(@RequestParam("topicIds") List<Long> topicIds) {
        return Result.ok(topicQueryService.listTopicWithUser(topicIds));
    }

    @DeleteMapping("/{topicId}")
    public Result<?> delete(@PathVariable @Positive Long topicId) {
        topicInfoWriteService.removeTopicInfoWithoutVerification(topicId);
        return Result.ok();
    }

}

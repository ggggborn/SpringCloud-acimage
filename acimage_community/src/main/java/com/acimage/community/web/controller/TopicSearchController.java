package com.acimage.community.web.controller;


import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.page.MyPage;
import com.acimage.common.redis.annotation.RequestLimit;
import com.acimage.common.redis.enums.LimitTarget;
import com.acimage.common.result.Result;
import com.acimage.community.model.request.TopicSearchReq;
import com.acimage.community.service.topic.TopicEsSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/community/topics/search")
@Validated
public class TopicSearchController {

    @Autowired
    TopicEsSearchService topicEsSearchService;

    @RequestLimit(limitTimes = {2}, durations = {1}, penaltyTimes = {-1}, targets = {LimitTarget.IP})
    @GetMapping("/multiSearch")
    public Result<MyPage<Topic>> searchTopics(@Validated @ModelAttribute TopicSearchReq topicSearchReq) {
        String search= topicSearchReq.getSearch();
        if(search!=null){
            topicSearchReq.setSearch(search.trim());
        }
        return Result.ok(topicEsSearchService.search(topicSearchReq));
    }


}

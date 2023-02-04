package com.acimage.community.web.controller;


import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.page.MyPage;
import com.acimage.common.result.Result;
import com.acimage.community.model.request.SearchTopicReq;
import com.acimage.community.service.topic.TopicSearchService;
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
    TopicSearchService topicSearchService;

    @GetMapping("/topics")
    public Result<MyPage<Topic>> searchTopics(@Validated @ModelAttribute SearchTopicReq searchTopicReq) {
        String search=searchTopicReq.getSearch();
        if(search!=null){
            searchTopicReq.setSearch(search.trim());
        }
        return Result.ok(topicSearchService.search(searchTopicReq));
    }
}

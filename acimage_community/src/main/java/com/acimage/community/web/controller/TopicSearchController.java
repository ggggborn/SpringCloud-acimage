package com.acimage.community.web.controller;


import com.acimage.common.global.annotation.Authentication;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.global.enums.AuthenticationType;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.page.Page;
import com.acimage.common.result.Result;
import com.acimage.community.global.annotation.TopicId;
import com.acimage.community.global.consts.PageSizeConsts;
import com.acimage.community.model.request.SearchTopicReq;
import com.acimage.community.model.vo.TopicInfoVo;
import com.acimage.community.service.topic.TopicInfoQueryService;
import com.acimage.community.service.topic.TopicSearchService;
import com.acimage.community.service.topic.annotation.RecordPageView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@RestController
@Slf4j
@RequestMapping("/api/community/topics/search")
@Validated
@Authentication
public class TopicSearchController {

    @Autowired
    TopicSearchService topicSearchService;


    @GetMapping("/searchTopics")
    public Result<Page<Topic>> searchTopics(@Validated @ModelAttribute SearchTopicReq searchTopicReq) {
        String search=searchTopicReq.getSearch();
        if(search!=null){
            searchTopicReq.setSearch(search.trim());
        }
        return Result.ok(topicSearchService.search(searchTopicReq));
    }
}

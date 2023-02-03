package com.acimage.admin.web.controller;


import com.acimage.admin.model.request.TopicQueryReq;
import com.acimage.admin.service.topic.TopicQueryService;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.result.Result;
import com.acimage.common.utils.LambdaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/admin/topics/query")
@Validated
public class TopicQueryController {
    @Autowired
    TopicQueryService topicQueryService;

    @GetMapping("/orderBy")
    public Result listOrderBy(@Validated @ModelAttribute TopicQueryReq topicQueryReq) {
        List<String> allowedColumns= LambdaUtils.columnsFrom(Topic::getUpdateTime,Topic::getCreateTime,Topic::getPageView,Topic::getStarCount,Topic::getCommentCount);
        if(!allowedColumns.contains(topicQueryReq.getColumn())){
            return Result.fail("非法查询字段");
        }
        return Result.ok(topicQueryService.listOrderByColumn(topicQueryReq));
    }
}

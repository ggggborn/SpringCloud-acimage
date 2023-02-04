package com.acimage.community.web.controller;


import com.acimage.common.model.domain.community.Tag;
import com.acimage.common.result.Result;
import com.acimage.community.service.tag.TagQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@Validated
@RequestMapping("/api/community/tags/query")
public class TagQueryController {
    @Autowired
    TagQueryService tagQueryService;

    @GetMapping("/all")
    public Result<List<Tag>> queryAll() {
        return Result.ok(tagQueryService.listAll());
    }
}

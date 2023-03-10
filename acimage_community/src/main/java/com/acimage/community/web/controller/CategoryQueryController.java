package com.acimage.community.web.controller;

import com.acimage.common.model.domain.community.Category;
import com.acimage.common.redis.annotation.RequestLimit;
import com.acimage.common.redis.enums.LimitTarget;
import com.acimage.common.result.Result;
import com.acimage.community.service.categoty.CategoryQueryService;
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
@RequestMapping("/api/community/categories/query")
public class CategoryQueryController {
    @Autowired
    CategoryQueryService categoryQueryService;

    @GetMapping("/all")
    public Result<List<Category>> queryAllCategories() {
        return Result.ok(categoryQueryService.listAll());
    }

}

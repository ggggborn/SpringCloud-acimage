package com.acimage.admin.web.controller;


import com.acimage.admin.service.category.CategoryQueryService;
import com.acimage.common.model.domain.community.Category;
import com.acimage.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/admin/categories")
@Validated
public class CategoryController {
    @Autowired
    CategoryQueryService categoryQueryService;

    @GetMapping("/all")
    public Result<List<Category>> queryAll(){
        return Result.ok(categoryQueryService.listAll()) ;
    }
}

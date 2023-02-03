package com.acimage.admin.service.category.impl;

import com.acimage.admin.dao.community.CategoryDao;
import com.acimage.admin.global.consts.ModuleConstants;
import com.acimage.admin.service.category.CategoryQueryService;
import com.acimage.common.model.domain.community.Category;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@DS(ModuleConstants.COMMUNITY)
@Service
public class CategoryQueryServiceImpl implements CategoryQueryService {
    @Autowired
    CategoryDao categoryDao;

    @Override
    public List<Category> listAll(){
        return categoryDao.selectList(null);
    }
}

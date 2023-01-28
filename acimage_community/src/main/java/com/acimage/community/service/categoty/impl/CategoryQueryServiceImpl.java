package com.acimage.community.service.categoty.impl;

import com.acimage.common.model.domain.community.Category;
import com.acimage.community.dao.CategoryDao;
import com.acimage.community.service.categoty.CategoryQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryQueryServiceImpl implements CategoryQueryService {

    @Autowired
    CategoryDao categoryDao;

    @Override
    public List<Category> listAll(){
        return categoryDao.selectList(null);
    }
}

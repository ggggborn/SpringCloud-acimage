package com.acimage.admin.service.api.impl;

import com.acimage.admin.dao.sys.ApiDao;
import com.acimage.admin.global.consts.ModuleConstants;
import com.acimage.admin.model.request.ApiQueryReq;
import com.acimage.admin.service.api.ApiQueryService;
import com.acimage.common.model.domain.sys.Api;
import com.acimage.common.model.page.MyPage;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@DS(ModuleConstants.SYS)
public class ApiQueryServiceImpl implements ApiQueryService {
    @Autowired
    ApiDao apiDao;

    @Override
    public MyPage<Api> pageBy(ApiQueryReq apiQueryReq) {
        IPage<Api> page = new Page<>(apiQueryReq.getPageNo(), apiQueryReq.getPageSize());
        LambdaQueryWrapper<Api> qw = new LambdaQueryWrapper<>();
        qw.orderByAsc(Api::getPath);
        String keyword = apiQueryReq.getKeyword();
        if (keyword != null) {
            qw.like(Api::getPath, keyword);
        }
        return MyPage.from(apiDao.selectPage(page, qw));
    }
}

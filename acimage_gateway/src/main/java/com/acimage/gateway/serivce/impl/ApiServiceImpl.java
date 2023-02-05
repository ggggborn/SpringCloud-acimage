package com.acimage.gateway.serivce.impl;

import com.acimage.common.model.domain.sys.Api;
import com.acimage.gateway.dao.ApiDao;
import com.acimage.gateway.serivce.ApiService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiServiceImpl implements ApiService {
    @Autowired
    ApiDao apiDao;

    @Override
    public List<Api> listEnableApis(){
        LambdaQueryWrapper<Api> qw=new LambdaQueryWrapper<>();
        qw.eq(Api::isEnable,true);
        return apiDao.selectList(qw);
    }
}

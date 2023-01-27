package com.acimage.admin.service.authorize.impl;

import com.acimage.admin.dao.sys.AuthorizeDao;
import com.acimage.admin.service.authorize.AuthorizeQueryService;
import com.acimage.common.model.domain.sys.Authorize;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("sys")
public class AuthorizeQueryServiceImpl implements AuthorizeQueryService {
    @Autowired
    AuthorizeDao authorizeDao;

    @Override
    public List<Authorize> listAuthorizeByRoleId(int roleId){
        LambdaQueryWrapper<Authorize> qw=new LambdaQueryWrapper<>();
        qw.eq(Authorize::getRoleId,roleId);
        return authorizeDao.selectList(qw);
    }
}

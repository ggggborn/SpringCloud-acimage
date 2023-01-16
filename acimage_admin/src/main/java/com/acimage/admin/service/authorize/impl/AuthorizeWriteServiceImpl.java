package com.acimage.admin.service.authorize.impl;

import com.acimage.admin.dao.sys.AuthorizeDao;
import com.acimage.admin.service.authorize.AuthorizeWriteService;
import com.acimage.admin.service.permission.PermissionQueryService;
import com.acimage.common.exception.BusinessException;
import com.acimage.common.model.domain.Authorize;
import com.acimage.common.model.domain.Permission;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@DS("sys")
public class AuthorizeWriteServiceImpl implements AuthorizeWriteService {
    @Autowired
    AuthorizeDao authorizeDao;
    @Autowired
    PermissionQueryService permissionQueryService;

    @Override
    public void save(int roleId, int permissionId){
        Permission permission=permissionQueryService.getPermission(permissionId);
        if(permission.getIsModule()){
            throw new BusinessException("该结点为模块，不可授权");
        }
        Authorize authorize=new Authorize(roleId,permissionId);
        authorizeDao.insert(authorize);
    }

    @Override
    public void remove(int roleId, int permissionId){
        LambdaQueryWrapper<Authorize> uw=new LambdaQueryWrapper<>();
        uw.eq(Authorize::getRoleId,roleId)
                .eq(Authorize::getPermissionId,permissionId);
        authorizeDao.delete(uw);
    }

}

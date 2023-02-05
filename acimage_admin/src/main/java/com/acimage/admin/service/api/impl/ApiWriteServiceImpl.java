package com.acimage.admin.service.api.impl;


import com.acimage.admin.dao.sys.ApiDao;
import com.acimage.admin.global.consts.ModuleConstants;
import com.acimage.admin.model.request.ApiAddReq;
import com.acimage.admin.model.request.ApiModifyReq;
import com.acimage.admin.service.api.ApiWriteService;
import com.acimage.admin.service.permission.PermissionQueryService;
import com.acimage.common.exception.BusinessException;
import com.acimage.common.model.domain.sys.Api;
import com.acimage.common.model.domain.sys.Permission;
import com.acimage.common.utils.common.BeanUtils;
import com.acimage.common.utils.common.ListUtils;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@DS(ModuleConstants.SYS)
public class ApiWriteServiceImpl implements ApiWriteService {
    @Autowired
    ApiDao apiDao;
    @Autowired
    PermissionQueryService permissionQueryService;

    @Override
    public void save(ApiAddReq apiAddReq){
        List<Permission> permissionList=permissionQueryService.listByModule(false);
        List<Integer> permissionIds= ListUtils.extract(Permission::getId,permissionList);
        if(!permissionIds.contains(apiAddReq.getPermissionId())){
            throw new BusinessException("权限不存在或非可用权限");
        }
        Api api= BeanUtils.copyPropertiesTo(apiAddReq,Api.class);
        api.setCreateTime(new Date());
        api.setUpdateTime(new Date());
        apiDao.insert(api);
    }

    @Override
    public void update(ApiModifyReq apiModifyReq){
        List<Permission> permissionList=permissionQueryService.listByModule(false);
        List<Integer> permissionIds= ListUtils.extract(Permission::getId,permissionList);
        if(!permissionIds.contains(apiModifyReq.getPermissionId())){
            throw new BusinessException("权限不存在或非可用权限");
        }
        Api api= BeanUtils.copyPropertiesTo(apiModifyReq,Api.class);
        api.setCreateTime(new Date());
        api.setUpdateTime(new Date());
        apiDao.updateById(api);
    }

    @Override
    public void delete(int id){
        apiDao.deleteById(id);
    }
}

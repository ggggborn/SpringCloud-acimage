package com.acimage.admin.service.permission.impl;

import com.acimage.admin.dao.sys.PermissionDao;
import com.acimage.admin.service.permission.PermissionQueryService;
import com.acimage.common.model.domain.sys.Permission;
import com.acimage.common.model.page.Page;
import com.acimage.common.utils.common.PageUtils;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("sys")
public class PermissionQueryServiceImpl implements PermissionQueryService {
    @Autowired
    PermissionDao permissionDao;

    @Override
    public Permission getPermission(int id){
        return permissionDao.selectById(id);
    }
    @Override
    public List<Permission> getPermissionTree(){
        return permissionDao.selectTreeByParentId(null);
    }
    @Override
    public Page<Permission> pagePermissionsWithParent(int pageNo,int pageSize){
        int startIndex= PageUtils.startIndexOf(pageNo,pageSize);
        List<Permission> permissionList= permissionDao.selectPermissionsWithParent(startIndex,pageSize);
        int count=permissionDao.selectCount(null);
        return new Page<>(count,permissionList);
    }

    @Override
    public List<Permission> listModules(){
        LambdaQueryWrapper<Permission> qw=new LambdaQueryWrapper<>();
        qw.eq(Permission::isModule,true);
        return permissionDao.selectList(qw);
    }
}

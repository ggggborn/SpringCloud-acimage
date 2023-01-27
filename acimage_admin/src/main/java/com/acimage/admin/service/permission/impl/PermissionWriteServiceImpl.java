package com.acimage.admin.service.permission.impl;

import cn.hutool.core.bean.BeanUtil;
import com.acimage.admin.dao.sys.PermissionDao;
import com.acimage.admin.model.request.PermissionAddReq;
import com.acimage.admin.model.request.PermissionModifyReq;
import com.acimage.admin.service.permission.PermissionWriteSercice;
import com.acimage.common.model.domain.sys.Permission;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@DS("sys")
public class PermissionWriteServiceImpl implements PermissionWriteSercice {

    @Autowired
    PermissionDao permissionDao;

    @Override
    public void save(PermissionAddReq permissionAddReq){
        Permission permission=new Permission();
        BeanUtil.copyProperties(permissionAddReq,permission);
        permissionDao.insert(permission);
    }

    @Override
    public void remove(int id){
        permissionDao.deleteById(id);
    }

    @Override
    public void update(PermissionModifyReq permissionModifyReq){
        Permission permission=new Permission();
        BeanUtil.copyProperties(permissionModifyReq,permission);
        permission.setUpdateTime(new Date());
        permissionDao.updateById(permission);
    }
}

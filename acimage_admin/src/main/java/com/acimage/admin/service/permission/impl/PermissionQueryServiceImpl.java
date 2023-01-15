package com.acimage.admin.service.permission.impl;

import com.acimage.admin.dao.sys.PermissionDao;
import com.acimage.admin.service.permission.PermissionQueryService;
import com.acimage.common.model.domain.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionQueryServiceImpl implements PermissionQueryService {
    @Autowired
    PermissionDao permissionDao;

    public List<Permission> getPermissionTree(){
        return null;
    }
}

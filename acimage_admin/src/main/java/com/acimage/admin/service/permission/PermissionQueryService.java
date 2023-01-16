package com.acimage.admin.service.permission;

import com.acimage.common.model.domain.Permission;
import com.acimage.common.model.page.Page;

import java.util.List;

public interface PermissionQueryService {
    List<Permission> getPermissionTree();

    Page<Permission> pagePermissionsWithParent(int pageNo,int pageSize);

    List<Permission> listModules();
}

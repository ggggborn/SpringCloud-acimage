package com.acimage.admin.service.permission;

import com.acimage.common.model.domain.sys.Permission;
import com.acimage.common.model.page.MyPage;

import java.util.List;

public interface PermissionQueryService {
    Permission getPermission(int id);

    List<Permission> getPermissionTree();

    MyPage<Permission> pagePermissionsWithParent(int pageNo, int pageSize);


    List<Permission> listByModule(boolean isModule);
}

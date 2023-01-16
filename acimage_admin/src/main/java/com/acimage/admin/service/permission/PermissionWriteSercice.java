package com.acimage.admin.service.permission;

import com.acimage.admin.model.request.PermissionAddReq;
import com.acimage.admin.model.request.PermissionModifyReq;
import org.springframework.transaction.annotation.Transactional;

public interface PermissionWriteSercice {
    void save(PermissionAddReq permissionAddReq);

    void remove(int id);

    @Transactional
    void update(PermissionModifyReq permissionModifyReq);
}

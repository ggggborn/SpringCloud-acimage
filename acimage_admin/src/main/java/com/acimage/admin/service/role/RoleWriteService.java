package com.acimage.admin.service.role;

import com.acimage.admin.model.request.RoleAddReq;
import com.acimage.admin.model.request.RoleModifyReq;
import com.acimage.common.model.domain.Role;

public interface RoleWriteService {


    void save(RoleAddReq roleAddReq);

    void remove(long id);

    void update(RoleModifyReq roleModifyReq);
}

package com.acimage.admin.service.role;

import com.acimage.admin.model.request.RoleAddReq;
import com.acimage.admin.model.request.RoleModifyReq;

public interface RoleWriteService {


    void save(RoleAddReq roleAddReq);

    void remove(long id);

    void update(RoleModifyReq roleModifyReq);
}

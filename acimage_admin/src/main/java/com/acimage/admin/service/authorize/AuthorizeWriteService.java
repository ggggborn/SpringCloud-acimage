package com.acimage.admin.service.authorize;

import com.acimage.common.model.domain.Authorize;

import java.util.List;

public interface AuthorizeWriteService {

    void save(int roleId, int permissionId);

    void remove(int roleId, int permissionId);
}

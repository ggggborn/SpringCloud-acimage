package com.acimage.admin.service.authorize;

public interface AuthorizeWriteService {

    void save(int roleId, int permissionId);

    void remove(int roleId, int permissionId);
}

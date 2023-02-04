package com.acimage.admin.service.userrole;

import java.util.List;
import java.util.Map;

public interface UserRoleWriteService {
    void save(long userId, int roleId);

    void remove(long userId, int roleId);
}

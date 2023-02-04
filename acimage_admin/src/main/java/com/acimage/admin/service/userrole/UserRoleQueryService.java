package com.acimage.admin.service.userrole;

import com.acimage.common.model.domain.sys.UserRole;

import java.util.List;
import java.util.Map;

public interface UserRoleQueryService {
    Map<Long, List<Integer>> listUserIdWithRoleIds(List<Long> userIds);

    UserRole getUserRole(long userId, int roleId);
}

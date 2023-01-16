package com.acimage.admin.service.authorize;

import com.acimage.common.model.domain.Authorize;

import java.util.List;

public interface AuthorizeQueryService {
    List<Authorize> listAuthorizeByRoleId(int roleId);
}

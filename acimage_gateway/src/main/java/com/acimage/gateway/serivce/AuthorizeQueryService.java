package com.acimage.gateway.serivce;

import com.acimage.common.model.domain.sys.Authorize;

import java.util.List;
import java.util.Map;

public interface AuthorizeQueryService {


    Map<Integer, List<Integer>> getRolePermissionIdsMap();

    List<Integer> listPermissionIds(Integer roleId);

    List<Authorize> listAll();
}

package com.acimage.gateway.serivce;

import java.util.List;

public interface UserRoleQueryService {
    List<Integer> listRoleIds(long userId);
}

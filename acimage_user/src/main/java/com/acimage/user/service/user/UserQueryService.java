package com.acimage.user.service.user;


import com.acimage.common.model.domain.user.User;

public interface UserQueryService {
    User getUser(long userId);
    Boolean isUsernameExist(String username);
}

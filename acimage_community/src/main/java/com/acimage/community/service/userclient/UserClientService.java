package com.acimage.community.service.userclient;

import com.acimage.common.model.domain.user.User;

public interface UserClientService {
    User queryUser(long userId);
}

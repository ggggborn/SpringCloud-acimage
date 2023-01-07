package com.acimage.community.service.userclient;

import com.acimage.common.model.domain.User;

public interface UserClientService {
    User queryUser(long userId);
}

package com.acimage.admin.service.user;

import com.acimage.admin.model.request.UserQueryReq;
import com.acimage.common.model.domain.user.User;
import com.acimage.common.model.page.MyPage;

public interface UserQueryService {
    MyPage<User> listBy(UserQueryReq userQueryReq);

    User getUser(long userId);
}

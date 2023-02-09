package com.acimage.admin.service.login;

import com.acimage.admin.model.request.AdminLoginReq;

public interface LoginService {
    String login(AdminLoginReq adminLoginReq);
}

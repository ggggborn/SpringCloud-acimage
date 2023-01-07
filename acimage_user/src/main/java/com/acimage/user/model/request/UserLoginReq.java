package com.acimage.user.model.request;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class UserLoginReq {

    @Size(min = 2, max = 12, message = "用户名长度在2到12之间")
    private String username;

    @Size(min = 100, max = 2000, message = "非法密码")
    String password;
}

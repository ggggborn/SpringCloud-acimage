package com.acimage.user.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class UserLoginReq {

    @Email(message = "邮箱格式错误")
    @Size(min=6,max=32,message = "邮箱长度在6到32之间")
    private String email;

    @Size(min = 100, max = 2000, message = "非法密码")
    String password;

    @Size(min=4,max=6,message = "验证码长度不符")
    String verifyCode;
}

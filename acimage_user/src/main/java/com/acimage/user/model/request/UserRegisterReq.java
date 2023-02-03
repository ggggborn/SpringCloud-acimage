package com.acimage.user.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterReq {
    public static final int VERIFY_CODE_LENGTH=8;

    @Size(min=2,max=12,message = "用户名长度在2到12之间")
    private String username;

    @Size(min=100,max=2000,message = "非法密码")
    private String password;

    @Email(message = "邮箱格式错误")
    @Size(min=6,max=32,message = "邮箱长度在6到32之间")
    private String email;

    /**
     * 有可能带空格
     */
    @Size(min=VERIFY_CODE_LENGTH,max = VERIFY_CODE_LENGTH+2)
    private String verifyCode;
}

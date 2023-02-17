package com.acimage.common.model.domain.user;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class UserPrivacy {

    @TableId(type = IdType.INPUT)
    private Long userId;
    private String pwd;
    private String salt;
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date registerTime;

    public UserPrivacy(Long userId, String pwd, String salt, String email) {
        this.userId = userId;
        this.pwd = pwd;
        this.salt = salt;
        this.email = email;
    }

}

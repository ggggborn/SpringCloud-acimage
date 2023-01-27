package com.acimage.common.model.domain.sys;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Role {
    public static final int ROLE_NAME_MAX=20;
    public static final int ROLE_NAME_MIN =2;
    public static final String ROLE_NAME_VALIDATION_MSG ="角色名在"+ ROLE_NAME_MIN +"-"+ROLE_NAME_MAX+"之间";
    public static final int NOTE_MAX=20;
    public static final String NOTE_VALIDATION_MSG ="备注小于"+ROLE_NAME_MAX+"字";

    @TableId(type = IdType.AUTO)
    Integer id;
    String roleName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
    String note;

}

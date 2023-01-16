package com.acimage.common.model.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class Permission {
    public static final int CODE_MAX=50;
    public static final int NOTE_MAX=20;
    public static final int LABEL_MAX=20;
    public static final String CODE_VALIDATION_MSG="权限码字数小于"+CODE_MAX;
    public static final String NOTE_VALIDATION_MSG="备注字数小于"+NOTE_MAX;
    public static final String LABEL_VALIDATION_MSG="标识字数小于"+LABEL_MAX;

    @TableId(type = IdType.AUTO)
    Integer id;
    Integer parentId;
    String code;
    String note;
    String label;
    Boolean isModule;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    @TableField(exist = false)
    List<Permission> children;
    @TableField(exist = false)
    Permission parent;


}

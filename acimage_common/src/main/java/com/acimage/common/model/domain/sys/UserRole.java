package com.acimage.common.model.domain.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    @TableId(type = IdType.INPUT)
    private Long id;
    private Long userId;
    private Integer roleId;
    private Date createTime;
}

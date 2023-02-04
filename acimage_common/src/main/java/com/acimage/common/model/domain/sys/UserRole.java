package com.acimage.common.model.domain.sys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    private Long id;
    private Long userId;
    private Integer roleId;
    private Date createTime;
}

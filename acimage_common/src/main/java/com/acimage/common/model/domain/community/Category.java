package com.acimage.common.model.domain.community;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Category {
    public static final int LABEL_MAX=50;
    public static final int LABEL_MIN =2;
    public static final String LABEL_VALIDATION_MSG ="角色名在"+ LABEL_MIN +"-"+LABEL_MAX+"之间";

    @TableId(type = IdType.AUTO)
    Integer id;
    String label;
    private Date createTime;
    private Date updateTime;

}

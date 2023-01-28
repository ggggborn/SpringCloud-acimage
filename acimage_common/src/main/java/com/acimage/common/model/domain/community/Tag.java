package com.acimage.common.model.domain.community;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    @TableId(type = IdType.AUTO)
    Integer id;
    String label;
    private Date createTime;
    private Date updateTime;
    @TableLogic(delval = "1")
    boolean deleted;
}

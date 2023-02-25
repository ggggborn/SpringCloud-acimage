package com.acimage.common.model.domain.user;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMsg {
    @TableId(type= IdType.INPUT)
    private Long userId;
    private Integer commentMsgCount;
    private Integer starMsgCount;

    private Date readCommentTime;
    private Date readStarTime;

}

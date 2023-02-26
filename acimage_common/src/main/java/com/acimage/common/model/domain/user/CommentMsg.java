package com.acimage.common.model.domain.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentMsg {
    @TableId(type = IdType.INPUT)
    Long commentId;
    String content;
    Long fromUserId;
    Long toUserId;
    Long topicId;
    String topicTitle;
    Date createTime;
    @TableField(exist = false)
    User user;
}

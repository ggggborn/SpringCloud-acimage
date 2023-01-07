package com.acimage.common.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Star {
    private Long userId;
    private Long topicId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField(exist = false)
    Topic topic;

    public Star(Long userId, Long topicId) {
        this.userId = userId;
        this.topicId = topicId;
    }
}

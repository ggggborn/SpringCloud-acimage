package com.acimage.common.model.domain.community;

import com.acimage.common.model.domain.user.User;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;


@Data
public class Comment {
    public static final int CONTENT_MIN=2;
    public static final int CONTENT_MAX=200;
    public static final String CONTENT_VALIDATION_MSG="评论字数在"+CONTENT_MIN+"-"+CONTENT_MAX+"之间";

    @TableId(type= IdType.INPUT)
    private Long id;
    private Long topicId;
    private Long userId;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @TableField(exist = false)
    Topic topic;
    @TableField(exist = false)
    User user;
}

package com.acimage.common.model.domain.community;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
public class Comment {
    public static final int CONTENT_MIN=2;
    public static final int CONTENT_MAX=200;
    public static final String CONTENT_VALIDATION_MSG="评论字数在"+CONTENT_MIN+"-"+CONTENT_MAX+"之间";

    @TableId(type= IdType.INPUT)
    private Long id;
    private Long topicId;
    private Long userId;
    private String content;
    @TableLogic
    private boolean deleted;
    private Date createTime;
    private Date updateTime;

    @TableField(exist = false)
    Topic topic;
    @TableField(exist = false)
    CmtyUser user;
}

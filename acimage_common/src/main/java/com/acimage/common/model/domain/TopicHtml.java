package com.acimage.common.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Data
public class TopicHtml {
    public static final int HTML_MIN = 4;
    public static final int HTML_MAX = 5000;
    public static final String HTML_VALIDATION_MSG = "内容太长，html代码长度在" + HTML_MIN + "-" + HTML_MAX + "之间";

    @TableId(type= IdType.INPUT)
    private Long topic_id;
    String html;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

}

package com.acimage.common.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
public class TopicHtml {
    public static final int HTML_MIN = 4;
    public static final int HTML_MAX = 5000;
    public static final String HTML_VALIDATION_MSG = "html源码长度在" + HTML_MIN + "-" + HTML_MAX + "之间";

    @TableId(type= IdType.INPUT)
    private Long topicId;
    String html;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

}

package com.acimage.common.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Image {
    public static final int DESCRIPTION_MIN = 2;
    public static final int DESCRIPTION_MAX = 30;
    public static final String DESCRIPTION_VALIDATION_MSG ="图片描述字数在"+DESCRIPTION_MIN+"-"+DESCRIPTION_MAX+"之间";

    public static final int FILE_NAME_MIN = 3;
    public static final int FILE_NAME_MAX = 80;
    public static final String FILE_NAME_VALIDATION_MSG ="文件名在"+FILE_NAME_MIN+"-"+FILE_NAME_MAX+"之间";

    @TableId(type= IdType.INPUT)
    private Long id;
    private Long topicId;
    private Integer size;
    private String fileName;
    private String description;
    private String url;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
    @TableLogic
    private boolean deleted;
    @TableField(exist = false)
    Topic topic;

    public Image(Long id, Long topicId, Integer size, String description) {
        this.id = id;
        this.topicId = topicId;
        this.size = size;
        this.description = description;
    }
}

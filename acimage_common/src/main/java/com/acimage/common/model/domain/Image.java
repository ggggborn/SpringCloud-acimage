package com.acimage.common.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Image {
    public static final int DESCRIPTION_MIN = 2;
    public static final int DESCRIPTION_MAX = 30;
    public static final String DESCRIPTION_VALIDATION_MSG ="图片描述字数在"+DESCRIPTION_MIN+"-"+DESCRIPTION_MAX+"之间";
    @TableId(type= IdType.INPUT)
    private Long id;
    private Long topicId;
    private Integer size;

    private String description;
    private String url;
    @TableField(exist = false)
    Topic topic;

    public Image(Long id, Long topicId, Integer size, String description) {
        this.id = id;
        this.topicId = topicId;
        this.size = size;
        this.description = description;
    }
}

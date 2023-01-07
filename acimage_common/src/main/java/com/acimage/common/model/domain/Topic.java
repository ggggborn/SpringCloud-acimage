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
public class Topic {
    public static final int CONTENT_MIN = 4;
    public static final int CONTENT_MAX = 500;
    public static final String CONTENT_VALIDATION_MSG= "内容长度在" + CONTENT_MIN + "-" + CONTENT_MAX + "之间";
    public static final int TITLE_MIN = 4;
    public static final int TITLE_MAX = 30;
    public static final String TITLE_VALIDATION_MSG= "标题长度在"+TITLE_MIN+"-"+TITLE_MAX+"之间";
    public static int IMAGES_AMOUNT_MAX=5;
    public static int IMAGES_AMOUNT_MIN=1;
    public static final String IMAGE_VALIDATION_MSG= "图片数量在"+IMAGES_AMOUNT_MIN+"-"+IMAGES_AMOUNT_MAX+"之间";

    @TableId(type= IdType.INPUT)
    private Long id;
    private Long userId;
    private String content;
    private String title;
    private Integer starCount;
    private Integer pageView;
    private Integer commentCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
    private String firstImageUrl;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    Date activityTime;

    @TableField(exist = false)
    User user;
    @TableField(exist = false)
    List<Image> images;
    @TableField(exist = false)
    List<Comment> comments;
}

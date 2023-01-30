package com.acimage.common.model.domain.community;


import com.acimage.common.model.domain.user.User;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    public static int TAG_MAX=3;
    public static int TAG_MIN=1;
    public static final String TAG_VALIDATION_MSG= "标签数量在"+TAG_MIN+"-"+TAG_MAX+"之间";

    @TableId(type= IdType.INPUT)
    private Long id;
    private Long userId;
    private String content;
    private String title;
    private Integer starCount;
    private Integer pageView;
    private Integer commentCount;
    private String coverImageUrl;
    private Integer categoryId;

    private Date activityTime;
    private Date createTime;
    private Date updateTime;

    @TableLogic(delval = "1")
    boolean deleted;

    @TableField(exist = false)
    CmtyUser user;
    @TableField(exist = false)
    Category category;
    @TableField(exist = false)
    List<Integer> tagIds;
//    @TableField(exist = false)
//    List<Image> images;
    @TableField(exist = false)
    List<Comment> comments;
}

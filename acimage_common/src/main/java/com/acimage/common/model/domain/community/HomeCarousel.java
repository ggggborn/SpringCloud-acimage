package com.acimage.common.model.domain.community;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeCarousel {
    public static final int DESC_MIN = 2;
    public static final int DESC_MAX = 30;
    public static final String DESC_INVALID_MSG ="图片描述字数在"+ DESC_MIN +"-"+ DESC_MAX +"之间";

    public static final int LINK_MAX = 100;
    public static final String LINK_INVALID_MSG ="图片描述字数在"+ 0 +"-"+ LINK_MAX +"之间";

    @TableId(type= IdType.AUTO)
    private Integer id;
    private String description;
    private String url;
    private String link;
    private Integer location;
    private Integer size;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

}

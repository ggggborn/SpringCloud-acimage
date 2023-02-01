package com.acimage.common.model.domain.image;

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

    @TableId(type= IdType.INPUT)
    private Long id;
    private String description;
    private String url;
    private Integer location;
    private Integer size;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    public HomeCarousel(Long id, String description, String url, Integer location, Integer size, Date createTime) {
        this.id = id;
        this.description = description;
        this.url = url;
        this.location = location;
        this.size = size;
        this.createTime = createTime;
    }
}
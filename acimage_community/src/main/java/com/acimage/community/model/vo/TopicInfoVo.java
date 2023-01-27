package com.acimage.community.model.vo;

import com.acimage.common.model.domain.Comment;
import com.acimage.common.model.domain.Image;
import com.acimage.common.model.domain.User;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class TopicInfoVo {


    private Long id;
    private Long userId;
    private String content;
    private String title;
    private Integer starCount;
    private Integer pageView;
    private Integer commentCount;
    private String coverImageUrl;

    private Date activityTime;
    private Date createTime;
    private Date updateTime;

    String html;
    User user;
    List<Comment> comments;
}

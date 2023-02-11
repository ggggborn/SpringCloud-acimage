package com.acimage.community.model.vo;

import com.acimage.common.model.domain.community.Comment;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.domain.user.User;
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
    private Integer categoryId;
    private List<Integer> tagIds;

    private Date activityTime;
    private Date createTime;
    private Date updateTime;

    String html;
    User user;
    List<Comment> comments;
    List<Topic> similarTopics;

}

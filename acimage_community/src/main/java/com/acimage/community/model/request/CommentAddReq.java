package com.acimage.community.model.request;


import com.acimage.common.model.domain.community.Comment;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class CommentAddReq {

    @Positive
    @NotNull(message = "话题id不能为空")
    private Long topicId;

    @Size(min = Comment.CONTENT_MIN, max = Comment.CONTENT_MAX, message = Comment.CONTENT_VALIDATION_MSG)
    private String content;

}

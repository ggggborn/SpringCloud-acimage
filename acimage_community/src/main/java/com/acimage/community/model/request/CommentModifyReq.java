package com.acimage.community.model.request;


import com.acimage.common.model.domain.community.Comment;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class CommentModifyReq {

    @Positive
    @NotNull(message = "id不能为空")
    Long id;

    @Size(min = Comment.CONTENT_MIN, max = Comment.CONTENT_MAX, message = Comment.CONTENT_VALIDATION_MSG)
    String content;
}

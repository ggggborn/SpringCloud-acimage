package com.acimage.community.service.comment;

import com.acimage.common.model.domain.Comment;

public interface CommentQueryService {
    Comment getComment(long commentId);
    Integer getCommentCount(long topicId);
}

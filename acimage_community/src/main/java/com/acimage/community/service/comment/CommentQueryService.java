package com.acimage.community.service.comment;

import com.acimage.common.model.domain.community.Comment;

public interface CommentQueryService {
    Comment getComment(long commentId);
    Integer getCommentCount(long topicId);
}

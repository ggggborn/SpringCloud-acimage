package com.acimage.community.service.comment;

import com.acimage.common.model.domain.community.Comment;
import com.acimage.common.model.page.Page;

import java.util.List;

public interface CommentInfoService {
    List<Comment> pageCommentsWithUser(long topicId, int pageNo);

    Page<Comment> pageCommentsWithTopicOrderByCreateTime(long userId, int pageNo);
}

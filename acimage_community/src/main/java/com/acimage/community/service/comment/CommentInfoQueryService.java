package com.acimage.community.service.comment;

import com.acimage.common.model.domain.community.Comment;
import com.acimage.common.model.page.MyPage;

import java.util.List;

public interface CommentInfoQueryService {
    List<Comment> pageCommentsWithUser(long topicId, int pageNo,int pageSize);

    MyPage<Comment> pageCommentsWithTopicOrderByCreateTime(long userId, int pageNo,int pageSize);
}

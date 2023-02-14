package com.acimage.admin.service.comment;

import com.acimage.admin.model.request.CommentQueryReq;
import com.acimage.common.model.domain.community.Comment;
import com.acimage.common.model.page.MyPage;

public interface CommentQueryService {
    MyPage<Comment> pageCommentsBy(CommentQueryReq commentQueryReq);
}

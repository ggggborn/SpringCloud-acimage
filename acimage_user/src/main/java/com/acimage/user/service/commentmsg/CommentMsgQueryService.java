package com.acimage.user.service.commentmsg;

import com.acimage.common.model.domain.user.CommentMsg;
import com.acimage.common.model.page.MyPage;

public interface CommentMsgQueryService {
    MyPage<CommentMsg> pageMyCommentMsg(long userId, int pageNo, int pageSize);
}

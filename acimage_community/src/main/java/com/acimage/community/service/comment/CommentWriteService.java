package com.acimage.community.service.comment;

import com.acimage.community.model.request.CommentAddReq;
import com.acimage.community.model.request.CommentModifyReq;
import org.springframework.transaction.annotation.Transactional;

public interface CommentWriteService {

    Integer saveComment(CommentAddReq commentAddReq);


    Integer removeComment(long commentId);

    Integer removeCommentWithoutVerification(long commentId);

    Integer removeComments(long topicId);

    Integer updateComment(CommentModifyReq commentModifyReq);

}

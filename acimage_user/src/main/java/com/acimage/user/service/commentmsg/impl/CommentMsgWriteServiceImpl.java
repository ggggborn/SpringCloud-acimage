package com.acimage.user.service.commentmsg.impl;

import com.acimage.common.model.domain.user.CommentMsg;
import com.acimage.user.dao.CommentMsgDao;
import com.acimage.user.service.commentmsg.CommentMsgWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentMsgWriteServiceImpl implements CommentMsgWriteService {
    @Autowired
    CommentMsgDao commentMsgDao;

    @Override
    public void insert(CommentMsg commentMsg){
        commentMsgDao.insert(commentMsg);
    }

}

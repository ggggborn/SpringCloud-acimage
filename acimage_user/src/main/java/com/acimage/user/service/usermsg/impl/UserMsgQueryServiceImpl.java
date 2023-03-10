package com.acimage.user.service.usermsg.impl;

import com.acimage.common.model.domain.user.UserMsg;
import com.acimage.user.dao.UserMsgDao;
import com.acimage.user.service.usermsg.UserMsgQueryService;
import com.acimage.user.web.websocket.MyWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
public class UserMsgQueryServiceImpl implements UserMsgQueryService {
    @Autowired
    UserMsgDao userMsgDao;

    @Override
    public Integer getMsgCount(long userId) {
        UserMsg userMsg = userMsgDao.selectById(userId);
        if (userMsg == null) {
            return null;
        }
        int commentMsgCount = userMsg.getCommentMsgCount();
        int starMsgCount = userMsg.getStarMsgCount();
        return commentMsgCount + starMsgCount;
    }


}

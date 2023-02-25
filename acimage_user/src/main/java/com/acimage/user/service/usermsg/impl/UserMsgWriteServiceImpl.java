package com.acimage.user.service.usermsg.impl;

import com.acimage.common.model.domain.user.UserMsg;
import com.acimage.common.utils.LambdaUtils;
import com.acimage.user.dao.UserMsgDao;
import com.acimage.user.service.usermsg.UserMsgWriteService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMsgWriteServiceImpl implements UserMsgWriteService {
    @Autowired
    UserMsgDao userMsgDao;

    @Override
    public void insert(long userId) {
        UserMsg userMsg = new UserMsg();
        userMsg.setUserId(userId);
        userMsgDao.insert(userMsg);
    }

    @Override
    public void increaseMsg(long userId, SFunction<UserMsg, ?> column, int increment) {
        String underlineColumn = LambdaUtils.underlineColumnNameOf(column);
        userMsgDao.increaseColumn(userId, underlineColumn, increment);
    }

    @Override
    public void resetMsgCount(long userId, SFunction<UserMsg, ?> column) {
        LambdaUpdateWrapper<UserMsg> uw = new LambdaUpdateWrapper<>();
        uw.eq(UserMsg::getUserId, userId)
                .set(UserMsg::getCommentMsgCount, 0);
        userMsgDao.update(null, uw);
    }
}

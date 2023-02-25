package com.acimage.user.service.usermsg;

import com.acimage.common.model.domain.user.UserMsg;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

public interface UserMsgWriteService {

    void insert(long userId);

    void increaseMsg(long userId, SFunction<UserMsg, ?> column, int increment);

    void resetMsgCount(long userId, SFunction<UserMsg, ?> column);
}

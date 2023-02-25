package com.acimage.user.service.usermsg;

import com.acimage.common.model.domain.user.UserMsg;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

public interface UserMsgQueryService {

    Integer getMsgCount(long userId);
}

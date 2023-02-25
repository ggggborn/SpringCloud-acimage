package com.acimage.user.service.commentmsg.impl;

import com.acimage.common.model.domain.user.CommentMsg;
import com.acimage.common.model.page.MyPage;
import com.acimage.common.utils.common.PageUtils;
import com.acimage.user.dao.CommentMsgDao;
import com.acimage.user.service.commentmsg.CommentMsgQueryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentMsgQueryServiceImpl implements CommentMsgQueryService {
    @Autowired
    CommentMsgDao commentMsgDao;

    @Override
    public MyPage<CommentMsg> pageMyCommentMsg(long userId, int pageNo, int pageSize) {
        LambdaQueryWrapper<CommentMsg> qw = new LambdaQueryWrapper<>();
        qw.eq(CommentMsg::getToUserId, userId);
        int startIndex = PageUtils.startIndexOf(pageNo, pageSize);
        List<CommentMsg> commentMsgList = commentMsgDao.selectCommentMsgsWithUser(userId, startIndex, pageSize);
        int totalCount = commentMsgDao.selectCount(qw);
        return new MyPage<>(totalCount, commentMsgList);
    }
}

package com.acimage.community.service.comment.impl;

import com.acimage.common.redis.annotation.KeyParam;
import com.acimage.common.redis.annotation.QueryRedis;
import com.acimage.common.model.domain.community.Comment;
import com.acimage.common.utils.common.PageUtils;
import com.acimage.community.dao.CommentDao;
import com.acimage.common.model.page.MyPage;
import com.acimage.community.service.comment.CommentInfoQueryService;
import com.acimage.community.global.consts.CommentKeyConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CommentInfoQueryServiceImpl implements CommentInfoQueryService {

    @Autowired
    CommentDao commentDao;

    @QueryRedis(keyPrefix = CommentKeyConstants.STRINGKP_TOPIC_COMMENTS, expire = 3L, unit = TimeUnit.SECONDS)
    public List<Comment> pageCommentsWithUser(long topicId, int pageNo, int pageSize) {
        //如果没有则查数据库
        int startIndex = PageUtils.startIndexOf(pageNo, pageSize);
        return commentDao.selectCommentsWithUser(topicId, startIndex, pageSize);
    }

    @QueryRedis(keyPrefix = CommentKeyConstants.STRINGKP_USER_COMMENTS, expire = 5, unit = TimeUnit.SECONDS)
    @Override
    public MyPage<Comment> pageCommentsWithTopicOrderByCreateTime(long userId, int pageNo, int pageSize) {
        int startIndex = PageUtils.startIndexOf(pageNo, pageSize);
        List<Comment> comments = commentDao.selectCommentsWithTopicOrderByCreateTime(userId, startIndex, pageSize);

        //查询总数
        LambdaQueryWrapper<Comment> qw = new LambdaQueryWrapper<>();
        qw.eq(Comment::getUserId, userId);
        int totalCount = commentDao.selectCount(qw);

        return new MyPage<>(totalCount, comments);
    }
}

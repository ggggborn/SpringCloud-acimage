package com.acimage.community.service.comment.impl;

import com.acimage.common.redis.annotation.KeyParam;
import com.acimage.common.redis.annotation.QueryRedis;
import com.acimage.common.model.domain.community.Comment;
import com.acimage.community.dao.CommentDao;
import com.acimage.community.service.comment.CommentQueryService;
import com.acimage.community.service.comment.consts.CommentKeyConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class CommentQueryServiceImpl implements CommentQueryService {
    @Autowired
    CommentDao commentDao;
    @QueryRedis(keyPrefix = "acimage:comments:id:",expire = 5L, unit = TimeUnit.SECONDS)
    @Override
    public Comment getComment(@KeyParam long commentId) {
        return commentDao.selectById(commentId);
    }

    @QueryRedis(keyPrefix = CommentKeyConstants.STRINGKP_COMMENT_COUNT,expire = 129L)
    @Override
    public Integer getCommentCount(@KeyParam long topicId) {
        LambdaQueryWrapper<Comment> qw=new LambdaQueryWrapper<>();
        qw.eq(Comment::getTopicId,topicId);
        return commentDao.selectCount(qw);
    }
}

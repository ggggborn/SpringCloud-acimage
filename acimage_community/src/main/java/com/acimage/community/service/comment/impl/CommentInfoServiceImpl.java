package com.acimage.community.service.comment.impl;

import com.acimage.common.redis.annotation.KeyParam;
import com.acimage.common.redis.annotation.QueryRedis;
import com.acimage.common.model.domain.Comment;
import com.acimage.community.dao.CommentDao;
import com.acimage.community.global.consts.PageSizeConsts;
import com.acimage.common.model.page.Page;
import com.acimage.community.service.comment.CommentInfoService;
import com.acimage.community.service.comment.consts.KeyConsts;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CommentInfoServiceImpl implements CommentInfoService {

    @Autowired
    CommentDao commentDao;

    @QueryRedis(keyPrefix = KeyConsts.STRINGKP_TOPIC_COMMENTS, expire = 0L)
    public List<Comment> pageCommentsWithUser(@KeyParam long topicId,
                                              @KeyParam(specials = {"1"}, expires = {37L}) int pageNo) {
//        final long expireMinutes = 37L;
//        String keyComments = KeyConstants.STRINGKP_TOPIC_COMMENTS + topicId;
//
//        List<Comment> comments;
//        if (pageNo == 1) {
//            //从redis获取数据
//            comments = redisUtils.getListFromString(keyComments, Comment.class);
//            if (comments != null) {
//                return comments;
//            }
//        }
//
//        //如果没有则查数据库
//        int startIndex = (pageNo - 1) * PageSizeConsts.COMMENTS_PAGE_SIZE;
//        int recordNumber = PageSizeConsts.COMMENTS_PAGE_SIZE;
//        comments = commentDao.selectCommentsWithUser(topicId, startIndex, recordNumber);
//
//        if (pageNo == 1) {
//            //设置到redis
//            redisUtils.setListAsString(keyComments, comments, expireMinutes, TimeUnit.MINUTES);
//        }
//        return comments;

        //如果没有则查数据库
        int startIndex = (pageNo - 1) * PageSizeConsts.TOPIC_COMMENTS;
        int recordNumber = PageSizeConsts.TOPIC_COMMENTS;
        return commentDao.selectCommentsWithUser(topicId, startIndex, recordNumber);
    }

    @QueryRedis(keyPrefix = "acimage:comments:userId:pageNo:",expire = 5,unit = TimeUnit.SECONDS)
    @Override
    public Page<Comment> pageCommentsWithTopicOrderByCreateTime(@KeyParam long userId,@KeyParam int pageNo) {
        int startIndex = (pageNo - 1) * PageSizeConsts.ACTIVITY_COMMENTS;
        int recordNumber = PageSizeConsts.ACTIVITY_TOPICS;
        List<Comment> comments = commentDao.selectCommentsWithTopicOrderByCreateTime(userId, startIndex, recordNumber);

        //查询总数
        LambdaQueryWrapper<Comment> qw = new LambdaQueryWrapper<>();
        qw.eq(Comment::getUserId, userId);
        int totalCount = commentDao.selectCount(qw);

        return new Page<>(totalCount, comments);
    }
}

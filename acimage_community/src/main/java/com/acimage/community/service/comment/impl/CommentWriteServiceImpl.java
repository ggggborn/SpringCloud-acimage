package com.acimage.community.service.comment.impl;


import cn.hutool.core.bean.BeanUtil;
import com.acimage.common.utils.SensitiveWordUtils;
import com.acimage.common.utils.common.ListUtils;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.exception.BusinessException;
import com.acimage.common.model.domain.community.Comment;
import com.acimage.community.model.request.CommentAddReq;
import com.acimage.community.model.request.CommentModifyReq;
import com.acimage.community.service.comment.CommentQueryService;
import com.acimage.community.service.comment.CommentWriteService;
import com.acimage.community.global.consts.CommentKeyConstants;
import com.acimage.common.utils.IdGenerator;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.community.dao.CommentDao;
import com.acimage.community.service.topic.TopicSpAttrWriteService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
@Slf4j
public class CommentWriteServiceImpl implements CommentWriteService {
    @Autowired
    CommentDao commentDao;
    @Autowired
    CommentQueryService commentQueryService;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    TopicSpAttrWriteService topicSpAttrWriteService;

    @Override
    public Integer saveComment(CommentAddReq commentAddReq) {
        Comment comment = new Comment();
        BeanUtil.copyProperties(commentAddReq, comment);

        long id = IdGenerator.getSnowflakeNextId();
        comment.setId(id);
        comment.setUserId(UserContext.getUserId());
        //敏感词过滤
        comment.setContent(SensitiveWordUtils.filter(commentAddReq.getContent()));
        int col = commentDao.insert(comment);

        long topicId = commentAddReq.getTopicId();
        //如果首页评论数未满，说明新增评论应在首页，所以删除redis相应数据
        String firstPageKey = CommentKeyConstants.keyOfTopicComments(topicId, 1);
        redisUtils.delete(firstPageKey);

        //更新评论数
        topicSpAttrWriteService.increaseCommentCount(topicId, 1);
        //更新话题的最新活跃时间
        topicSpAttrWriteService.changeActivityTime(topicId, new Date());

        return col;
    }


    @Override
    public Integer removeComment(long commentId) {
        Comment comment = commentQueryService.getComment(commentId);
        if (comment == null) {
            log.error("user:{} 删除评论{} 错误：评论不存在", UserContext.getUsername(), commentId);
            throw new BusinessException("评论不存在");
        }
        if (!comment.getUserId().equals(UserContext.getUserId())) {
            log.error("user:{} 删除 评论{} 错误：非评论持有者", UserContext.getUsername(), commentId);
            throw new BusinessException("非法操作");
        }
        int col = commentDao.deleteById(commentId);

        //如果影响redis话题首页评论，则删除
        long topicId = comment.getTopicId();
        String firstPageKey = CommentKeyConstants.keyOfTopicComments(topicId, 1);
        List<Comment> comments = redisUtils.getListFromString(firstPageKey, Comment.class);
        if (comments != null && ListUtils.extract(Comment::getId, comments).contains(commentId)) {
            redisUtils.delete(firstPageKey);
        }

        //更新评论数
        topicSpAttrWriteService.increaseCommentCount(topicId, -1);

        return col;
    }

    /**
     * 删除话题时调用
     */
    @Override
    public Integer removeComments(long topicId) {
        int col = commentDao.deleteByTopicId(topicId);
        //删除redis数据
        redisUtils.delete(CommentKeyConstants.STRINGKP_TOPIC_COMMENTS + topicId);
        return col;
    }

    @Override
    public Integer updateComment(CommentModifyReq commentModifyReq) {

        Comment modifiedComment = new Comment();
        BeanUtil.copyProperties(commentModifyReq, modifiedComment, false);

        modifiedComment.setUpdateTime(new Date());
        log.info("评论修改为{}", modifiedComment);

        long commentId = commentModifyReq.getId();

        LambdaUpdateWrapper<Comment> uw = new LambdaUpdateWrapper<>();
        uw.eq(Comment::getId, commentId)
                .eq(Comment::getUserId, UserContext.getUserId());
        int col = commentDao.update(modifiedComment, uw);

        if (col == 0) {
            log.error("用户{} 修改 评论{} 错误：评论已被删除或评论非当前用户所有", UserContext.getUsername(), commentId);
            throw new BusinessException("更新失败");
        }

        //找到评论
        Comment originComment = commentQueryService.getComment(commentId);

        //如果影响redis话题首页评论，则删除redis数据
        long topicId = originComment.getTopicId();
        String firstPageCommentsKey = CommentKeyConstants.keyOfTopicComments(topicId, 1);
        List<Comment> comments = redisUtils.getListFromString(firstPageCommentsKey, Comment.class);
        if (comments != null && ListUtils.extract(Comment::getId, comments).contains(commentId)) {
            redisUtils.delete(firstPageCommentsKey);
        }

        //更新对应话题的最新活跃时间
        topicSpAttrWriteService.changeActivityTime(topicId, new Date());
        return col;
    }

}

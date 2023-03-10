package com.acimage.community.service.comment.impl;


import cn.hutool.core.bean.BeanUtil;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.domain.user.CommentMsg;
import com.acimage.common.utils.SensitiveWordUtils;
import com.acimage.common.utils.common.BeanUtils;
import com.acimage.common.utils.common.ListUtils;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.global.exception.BusinessException;
import com.acimage.common.model.domain.community.Comment;
import com.acimage.community.listener.event.CommentEvent;
import com.acimage.community.model.request.CommentAddReq;
import com.acimage.community.model.request.CommentModifyReq;
import com.acimage.community.mq.producer.UserMsgMqProducer;
import com.acimage.community.service.comment.CommentQueryService;
import com.acimage.community.service.comment.CommentWriteService;
import com.acimage.community.global.consts.CommentKeyConstants;
import com.acimage.common.utils.IdGenerator;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.community.dao.CommentDao;
import com.acimage.community.service.topic.TopicQueryService;
import com.acimage.community.service.topic.TopicSpAttrWriteService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


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
    @Autowired
    TopicQueryService topicQueryService;
    @Autowired
    UserMsgMqProducer userMsgMqProducer;

    @Override
    public Integer saveComment(CommentAddReq commentAddReq) {
        String publishedContent = redisUtils.getForString(CommentKeyConstants.STRINGKP_PUBLISHED_COMMENTS);
        if (publishedContent != null && publishedContent.equals(commentAddReq.getContent())) {
            log.warn("user:{}?????????????????? title:{}", UserContext.getUsername(), commentAddReq.getContent());
            throw new BusinessException("???????????????????????????????????????????????????");
        }
        Comment comment = new Comment();
        BeanUtil.copyProperties(commentAddReq, comment);

        Date now = new Date();
        long id = IdGenerator.getSnowflakeNextId();
        comment.setId(id);
        comment.setUserId(UserContext.getUserId());
        comment.setCreateTime(now);
        comment.setUpdateTime(now);
        //???????????????
        String filterContent = SensitiveWordUtils.filter(commentAddReq.getContent());
        comment.setContent(filterContent);
        int col = commentDao.insert(comment);

        long topicId = commentAddReq.getTopicId();
        //??????????????????
        String firstPageKey = CommentKeyConstants.keyOfTopicComments(topicId, 1);
        redisUtils.delete(firstPageKey);
        //???????????????
        topicSpAttrWriteService.increaseCommentCount(topicId, col);
        //?????????????????????????????????
        topicSpAttrWriteService.changeActivityTime(topicId, new Date());

        //????????????????????????
        Topic topic = topicQueryService.getTopic(topicId);
        CommentMsg commentMsg = CommentMsg.builder()
                .commentId(id)
                .content(filterContent)
                .createTime(now)
                .fromUserId(UserContext.getUserId())
                .toUserId(topic.getUserId())
                .topicTitle(topic.getTitle())
                .topicId(topicId)
                .build();
        userMsgMqProducer.sendCommentMessage(commentMsg);

        long timeout = 10L;
        redisUtils.setAsString(CommentKeyConstants.STRINGKP_PUBLISHED_COMMENTS + UserContext.getUserId(),
                commentAddReq.getContent(), timeout, TimeUnit.SECONDS);

        return col;
    }


    @Override
    public Integer removeComment(long commentId) {
        Comment comment = commentQueryService.getComment(commentId);
        if (comment == null) {
            log.error("user:{} ????????????{} ????????????????????????", UserContext.getUsername(), commentId);
            throw new BusinessException("???????????????");
        }
        if (!comment.getUserId().equals(UserContext.getUserId())) {
            log.error("user:{} ?????? ??????{} ???????????????????????????", UserContext.getUsername(), commentId);
            throw new BusinessException("????????????");
        }
        int col = commentDao.deleteById(commentId);

        //????????????redis??????????????????????????????
        long topicId = comment.getTopicId();
        String firstPageKey = CommentKeyConstants.keyOfTopicComments(topicId, 1);

        redisUtils.delete(firstPageKey);

        //???????????????
        topicSpAttrWriteService.increaseCommentCount(topicId, -col);

        return col;
    }

    @Override
    public Integer removeCommentWithoutVerification(long commentId) {
        Comment comment = commentQueryService.getComment(commentId);
        if (comment == null) {
            log.error("user:{} ????????????{} ????????????????????????", UserContext.getUsername(), commentId);
            throw new BusinessException("???????????????");
        }
        int col = commentDao.deleteById(commentId);

        //????????????redis??????????????????????????????
        long topicId = comment.getTopicId();
        String firstPageKey = CommentKeyConstants.keyOfTopicComments(topicId, 1);

        redisUtils.delete(firstPageKey);

        //???????????????
        topicSpAttrWriteService.increaseCommentCount(topicId, -col);

        return col;
    }

    /**
     * ?????????????????????
     */
    @Override
    public Integer removeComments(long topicId) {
        int col = commentDao.deleteByTopicId(topicId);
        //??????redis??????
        redisUtils.delete(CommentKeyConstants.STRINGKP_TOPIC_COMMENTS + topicId);
        return col;
    }

    @Override
    public Integer updateComment(CommentModifyReq commentModifyReq) {
        Date now = new Date();

        Comment modifiedComment = BeanUtils.copyPropertiesTo(commentModifyReq, Comment.class);
        //???????????????
        String filterContent = SensitiveWordUtils.filter(commentModifyReq.getContent());
        modifiedComment.setContent(filterContent);

        modifiedComment.setUpdateTime(now);
        log.info("???????????????{}", modifiedComment);

        long commentId = commentModifyReq.getId();

        LambdaUpdateWrapper<Comment> uw = new LambdaUpdateWrapper<>();
        uw.eq(Comment::getId, commentId)
                .eq(Comment::getUserId, UserContext.getUserId());
        int col = commentDao.update(modifiedComment, uw);

        if (col == 0) {
            log.error("??????{} ?????? ??????{} ?????????????????????????????????????????????????????????", UserContext.getUsername(), commentId);
            throw new BusinessException("???????????????????????????");
        }

        //????????????
        Comment originComment = commentQueryService.getComment(commentId);

        //????????????redis??????????????????????????????redis??????
        long topicId = originComment.getTopicId();
        String firstPageCommentsKey = CommentKeyConstants.keyOfTopicComments(topicId, 1);
        List<Comment> comments = redisUtils.getListFromString(firstPageCommentsKey, Comment.class);
        if (comments != null && ListUtils.extract(Comment::getId, comments).contains(commentId)) {
            redisUtils.delete(firstPageCommentsKey);
        }

        //???????????????????????????????????????
        topicSpAttrWriteService.changeActivityTime(topicId, now);
        return col;
    }

}

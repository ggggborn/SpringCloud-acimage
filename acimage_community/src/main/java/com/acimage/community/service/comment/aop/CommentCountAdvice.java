package com.acimage.community.service.comment.aop;


import com.acimage.common.utils.common.AopUtils;
import com.acimage.common.utils.RedisUtils;
import com.acimage.community.global.annotation.CommentId;
import com.acimage.community.global.annotation.TopicId;
import com.acimage.community.service.comment.CommentQueryService;
import com.acimage.community.service.comment.annotation.Operation;
import com.acimage.community.service.comment.annotation.UpdateCcByReturn;
import com.acimage.community.service.comment.consts.KeyConsts;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 在评论的增删之后更改对应话题的评论数
 */
@Deprecated
@Slf4j
public class CommentCountAdvice {
    private static final String POINT_CUT = "@annotation(com.acimage.community.service.comment.annotation.UpdateCcByReturn)";
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    CommentQueryService commentQueryService;

    @Pointcut(POINT_CUT)
    public void updateCommentCountPointCut() {}

    @AfterReturning(pointcut = "updateCommentCountPointCut()", returning = "result")
    public Object around(JoinPoint joinPoint, Object result) throws Throwable {
        if (result == null) {
            log.warn("返回值为空，不更新commentCount");
            return null;
        }

        //方法无参数直接返回
        if (!AopUtils.hasParameters(joinPoint)) {
            return result;
        }

        //首先获取topicId
        Long topicId = AopUtils.annotatedArgOrArgFieldOf(joinPoint, TopicId.class, Long.class);
        if (topicId == null) {
            return result;
        } else {
            //如果获取不到topicId，通过commentId找到评论再得到topicId
            Long commentId = AopUtils.annotatedArgOrArgFieldOf(joinPoint, CommentId.class, Long.class);
            if (commentId == null) {
                log.warn("找不到topicId和commentId:入参及成员变量无@TopicId、@CommentId注解");
                return result;
            }
            topicId = commentQueryService.getComment(commentId).getTopicId();
        }

        String commentCountKey = KeyConsts.STRINGKP_COMMENT_COUNT + topicId;

        //获取评论数变化量
        int variance = 0;
        Operation operation = AopUtils.annotationFrom(joinPoint, UpdateCcByReturn.class).operation();
        if(operation==Operation.REMOVE_ALL){
            redisUtils.delete(commentCountKey);
        }else if (operation == Operation.SUB) {
            variance = -(int) result;
        }else if (operation==Operation.ADD){
            variance = (int) result;
        }
        redisUtils.incrementIfPresent(commentCountKey, variance);

        return result;
    }

}

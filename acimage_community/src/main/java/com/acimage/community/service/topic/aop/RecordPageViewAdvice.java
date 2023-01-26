package com.acimage.community.service.topic.aop;


import com.acimage.common.global.context.UserContext;
import com.acimage.common.utils.common.AopUtils;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.community.global.annotation.TopicId;
import com.acimage.community.service.topic.TopicSpAttrQueryService;
import com.acimage.community.service.topic.consts.KeyConstants;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class RecordPageViewAdvice {
    private static final String POINT_CUT= "@annotation(com.acimage.community.service.topic.annotation.RecordPageView)";
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    TopicSpAttrQueryService topicSpAttrQueryService;

    @Pointcut(POINT_CUT)
    public void recordPageViewPointCut() {
    }

    @AfterReturning(pointcut = "recordPageViewPointCut()", returning = "result")
    public Object around(JoinPoint joinPoint, Object result) throws Throwable {

        Long topicId=AopUtils.annotatedArgOrArgFieldOf(joinPoint, TopicId.class, Long.class);
        if (topicId==null) {
            return result;
        }

        String ipAddress = UserContext.getIp();

        // 获取存入的key
        String topicPvLogKey = KeyConstants.LOGKP_TOPIC_PV + topicId;

        //记录哪些ip访问过这个话题，以下这两行代码顺序不可交换！
        Long count = redisUtils.addForHyperLogLog(topicPvLogKey, ipAddress);
        //记录当前哪些话题有在统计浏览量
        redisUtils.addForSet(KeyConstants.SETK_RECORDING_PV_INCREMENT, topicId.toString());

        if (count == 0) {
            log.info("ip：{} 已访问过 话题{}", ipAddress, topicId);
        } else {
            log.info("ip：{} 访问话题{} 累计访问量 {}", ipAddress, topicId, redisUtils.sizeForHyperLogLog(topicPvLogKey));
            //更新浏览量排行榜
            redisUtils.addForZSet(KeyConstants.ZSETK_TOPIC_PV, topicId.toString(), topicSpAttrQueryService.getPageView(topicId));
        }

        return result;
    }

}

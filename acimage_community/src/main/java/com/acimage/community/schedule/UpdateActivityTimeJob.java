package com.acimage.community.schedule;

import cn.hutool.core.collection.CollectionUtil;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.community.service.topic.TopicSpAttrWriteService;
import com.acimage.community.global.consts.TopicKeyConstants;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 不再定时任务更新活跃时间，直接在增加评论或修改话题的时候直接更新
 */
@Deprecated
@Slf4j
public class UpdateActivityTimeJob extends QuartzJobBean {

    @Autowired
    RedisUtils redisUtils;
    @Autowired
    TopicSpAttrWriteService topicSpAttrWriteService;

    /**
     * 从redis中获取话题的新增浏览量并写入到数据库中
     */
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        //批量更新到数据库的大小
        final int BATCH_SIZE = 10;
        log.info("start 系统定时任务：保存活跃时间");
        //获取哪些话题评论数有变化
        List<Long> topicIdList = redisUtils.membersForSet(TopicKeyConstants.SETK_RECORDING_ACTIVITY_TIME, Long.class);
        if (CollectionUtil.isEmpty(topicIdList)) {
            return;
        }

        StringBuilder logString = new StringBuilder();

        int index = 0;

        //获取话题id，评论数增量，相应redis的key
        List<String> activityTimeKeys = new ArrayList<>(BATCH_SIZE);
        List<Long> batchTopicIds = new ArrayList<>(BATCH_SIZE);
        List<Date> batchActivityTime = new ArrayList<>(BATCH_SIZE);

        for (Long topicId : topicIdList) {

            index++;
            String activityTimeKey = TopicKeyConstants.STRINGKP_TOPIC_ACTIVITY_TIME + topicId;

            //获取活跃时间
            Date activityTime = redisUtils.getObjectFromString(activityTimeKey, Date.class);

            if (activityTime != null) {
                batchTopicIds.add(topicId);
                batchActivityTime.add(activityTime);
            }
            activityTimeKeys.add(activityTimeKey);

            //日志记录浏览量增加的信息
            logString.append(String.format("%s活跃时间更新为%s ", topicId, activityTime));

            if (index % BATCH_SIZE == 0 || index == topicIdList.size()) {
                //数据库批量增加浏览量
                try {
                    topicSpAttrWriteService.updateBatchActivityTime(batchTopicIds, batchActivityTime);
                } catch (Exception e) {
                    log.error("更新activityTime失败id:{} activityTime:{}",batchTopicIds,batchActivityTime);
                }

                //批量移除对应值或删除对应键值，这两者顺序不可交换！
                redisUtils.removeForSet(TopicKeyConstants.SETK_RECORDING_ACTIVITY_TIME, batchTopicIds);
                redisUtils.delete(activityTimeKeys);

                log.info(logString.toString());

                //清空
                batchTopicIds.clear();
                batchActivityTime.clear();
                activityTimeKeys.clear();
                //重新初始化
                index=0;
                logString = new StringBuilder();
            }
        }


        log.info("end 系统定时任务：保存活跃时间");
    }
}

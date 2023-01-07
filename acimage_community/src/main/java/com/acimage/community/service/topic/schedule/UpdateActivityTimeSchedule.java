package com.acimage.community.service.topic.schedule;

import cn.hutool.core.collection.CollectionUtil;
import com.acimage.common.utils.RedisUtils;
import com.acimage.community.service.topic.TopicSpAttrWriteService;
import com.acimage.community.service.topic.consts.KeyConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class UpdateActivityTimeSchedule {
    private final long FIXED_RATE_MINUTES = 37L;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    TopicSpAttrWriteService topicSpAttrWriteService;

    /**
     * 从redis中获取话题的新增浏览量并写入到数据库中
     */
    @Scheduled(fixedRate = FIXED_RATE_MINUTES, timeUnit = TimeUnit.MINUTES)
    private void saveActivityTimeTask() {
        //批量更新到数据库的大小
        final int BATCH_SIZE = 10;

        //获取哪些话题评论数有变化
        List<Long> topicIdList = redisUtils.membersForSet(KeyConstants.SETK_RECORDING_ACTIVITY_TIME, Long.class);
        if (CollectionUtil.isEmpty(topicIdList)) {
            return;
        }
        log.info("start 系统定时任务：保存活跃时间");
        StringBuilder logString = new StringBuilder();

        int index = 0;

        //获取话题id，评论数增量，相应redis的key
        List<String> activityTimeKeys = new ArrayList<>(BATCH_SIZE);
        List<Long> batchTopicIds = new ArrayList<>(BATCH_SIZE);
        List<Date> batchActivityTime = new ArrayList<>(BATCH_SIZE);

        for (Long topicId : topicIdList) {

            index++;
            String activityTimeKey = KeyConstants.STRINGKP_TOPIC_ACTIVITY_TIME + topicId;

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
                    topicSpAttrWriteService.updateActivityTime(batchTopicIds, batchActivityTime);
                } catch (Exception e) {
                    log.error("更新activityTime失败id:{} activityTime:{}",batchTopicIds,batchActivityTime);
                }

                //批量移除对应值或删除对应键值，这两者顺序不可交换！
                redisUtils.removeForSet(KeyConstants.SETK_RECORDING_ACTIVITY_TIME, batchTopicIds);
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

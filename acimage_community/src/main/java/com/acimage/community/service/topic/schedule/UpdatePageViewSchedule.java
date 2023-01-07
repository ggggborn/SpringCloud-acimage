package com.acimage.community.service.topic.schedule;

import cn.hutool.core.collection.CollectionUtil;
import com.acimage.common.utils.RedisUtils;
import com.acimage.community.service.topic.TopicSpAttrWriteService;
import com.acimage.community.service.topic.consts.KeyConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class UpdatePageViewSchedule {
    private final long FIXED_RATE_MINUTES = 57L;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    TopicSpAttrWriteService topicSpAttrWriteService;

    /**
     * 从redis中获取话题的新增浏览量并写入到数据库中
     */
    @Scheduled(fixedRate = FIXED_RATE_MINUTES, timeUnit = TimeUnit.MINUTES)
    private void savePageViewTask() {
        //批量更新到数据库的大小
        final int BATCH_SIZE = 10;

        //获取哪些话题被记录了浏览量
        List<Long> topicIdList = redisUtils.membersForSet(KeyConstants.SETK_RECORDING_PV_INCREMENT, Long.class);
        if (CollectionUtil.isEmpty(topicIdList)) {
            return;
        }
        log.info("start 系统定时任务：保存浏览量变化");
        StringBuilder logString = new StringBuilder();
        int index = 0;

        List<String> batchPvLogKeys = new ArrayList<>(BATCH_SIZE);
        List<Long> batchTopicIds = new ArrayList<>(BATCH_SIZE);
        List<Integer> batchPvIncrements = new ArrayList<>(BATCH_SIZE);

        for (Long topicId : topicIdList) {
            index++;
            String pvLogKey = KeyConstants.LOGKP_TOPIC_PV + topicId;
            Long pvIncrement = redisUtils.sizeForHyperLogLog(pvLogKey);

            //记录话题id，浏览量增量，相应redis的key、value
            if (pvIncrement != null) {
                batchTopicIds.add(topicId);
                batchPvIncrements.add(pvIncrement.intValue());
            } else {
                redisUtils.removeForSet(KeyConstants.SETK_RECORDING_PV_INCREMENT,Long.toString(topicId) );
            }
            batchPvLogKeys.add(pvLogKey);

            //日志记录浏览量增加的信息
            logString.append(String.format("%s增加浏览量%d ", topicId, pvIncrement));

            if (index % BATCH_SIZE == 0 || index == topicIdList.size()) {
                //数据库批量增加浏览量
                try {
                    topicSpAttrWriteService.updatePageViewByIncrement(batchTopicIds, batchPvIncrements);
                } catch (Exception e) {
                    log.error("error:更新pageView变化失败 ids:{} increments:{}",batchTopicIds,batchPvIncrements);
                }

                //批量移除对应值或删除对应键值，这两者顺序不可交换！
                redisUtils.removeForSet(KeyConstants.SETK_RECORDING_PV_INCREMENT, batchTopicIds);
                redisUtils.delete(batchPvLogKeys);

                //清空
                batchTopicIds.clear();
                batchPvIncrements.clear();
                batchPvLogKeys.clear();
                //重新初始化
                index = 0;
                log.info(logString.toString());
                logString = new StringBuilder();
            }
        }


        log.info("end 系统定时任务：保存浏览量变化");
    }
}

package com.acimage.community.service.topic.schedule;

import cn.hutool.core.collection.CollectionUtil;
import com.acimage.common.model.domain.Topic;
import com.acimage.common.utils.LambdaUtils;
import com.acimage.common.utils.RedisUtils;
import com.acimage.community.service.topic.TopicSpAttrWriteService;
import com.acimage.community.service.topic.consts.KeyConstants;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Component
@Slf4j
public class UpdateStarCountJob extends QuartzJobBean {
    private final long FIXED_RATE_MINUTES = 11L;
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
        log.info("START 系统定时任务：保存收藏数变化");
        //获取哪些话题评论数有变化
        List<Long> topicIdList = redisUtils.membersForSet(KeyConstants.SETK_RECORDING_STAR_COUNT_INCREMENT, Long.class);
        if (CollectionUtil.isEmpty(topicIdList)) {
            return;
        }


        StringBuilder logString = new StringBuilder();
        int index = 0;


        List<Long> batchTopicIds = new ArrayList<>(BATCH_SIZE);
        List<Integer> batchScIncrements = new ArrayList<>(BATCH_SIZE);

        for (Long topicId : topicIdList) {
            index++;
            String scIncrementKey = KeyConstants.STRINGKP_TOPIC_STAR_COUNT_INCREMENT + topicId;
            String hashKeyForTopic= KeyConstants.HASHKP_TOPIC+topicId;
            String fieldName= LambdaUtils.getCamelColumnName(Topic::getStarCount);
            Long scIncrement = redisUtils.getAndCombineAndDelete(scIncrementKey, hashKeyForTopic, fieldName);


            //记录话题id，评论数增量，相应redis的key、value
            if (scIncrement != null) {
                batchTopicIds.add(topicId);
                batchScIncrements.add(scIncrement.intValue());
            }else{
                redisUtils.removeForSet(KeyConstants.SETK_RECORDING_STAR_COUNT_INCREMENT,Long.toString(topicId));
            }


            //日志记录浏览量增加的信息
            logString.append(String.format("%s收藏变化量为%d ", topicId, scIncrement));

            if (index % BATCH_SIZE == 0 || index == topicIdList.size()) {
                //数据库批量增加浏览量
                try {
                    topicSpAttrWriteService.updateStarCountByIncrement(batchTopicIds, batchScIncrements);
                } catch (Exception e) {
                    log.error("error:更新starCount变化失败 ids:{} increments:{}",batchTopicIds,batchScIncrements);
                }

                //批量移除对应值或删除对应键值，这两者顺序不可交换！
                redisUtils.removeForSet(KeyConstants.SETK_RECORDING_STAR_COUNT_INCREMENT, batchTopicIds);

                log.info(logString.toString());

                //清空
                batchTopicIds.clear();
                batchScIncrements.clear();

                //重新初始化
                index=0;
                logString = new StringBuilder();
            }
        }

        log.info("END 系统定时任务：保存收藏数变化");
    }
}

package com.acimage.community.schedule;


import cn.hutool.core.collection.CollectionUtil;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.utils.LambdaUtils;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.community.service.topic.TopicSpAttrWriteService;
import com.acimage.community.global.consts.TopicKeyConstants;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.ArrayList;
import java.util.List;

@Deprecated
@Slf4j
public class UpdateCommentCountJob extends QuartzJobBean {

    @Autowired
    RedisUtils redisUtils;
    @Autowired
    TopicSpAttrWriteService topicSpAttrWriteService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        //批量更新到数据库的大小
        final int BATCH_SIZE = 10;
        log.info("start 系统定时任务：保存评论数变化");
        //获取哪些话题评论数有变化
        List<Long> topicIdList = redisUtils.membersForSet(TopicKeyConstants.SETK_RECORDING_COMMENT_COUNT_INCREMENT, Long.class);
        if (CollectionUtil.isEmpty(topicIdList)) {
            return;
        }
        StringBuilder logString = new StringBuilder();

        int index = 0;

        List<Long> batchTopicIds = new ArrayList<>(BATCH_SIZE);
        List<Integer> batchCcIncrements = new ArrayList<>(BATCH_SIZE);

        for (Long topicId : topicIdList) {

            index++;
            String ccIncrementKey = TopicKeyConstants.STRINGKP_TOPIC_COMMENT_COUNT_INCREMENT + topicId;
            String hashKeyForTopic= TopicKeyConstants.HASHKP_TOPIC+topicId;
            String fieldName=LambdaUtils.columnOf(Topic::getCommentCount);
            Long ccIncrement = redisUtils.getAndCombineAndDelete(ccIncrementKey, hashKeyForTopic, fieldName);

            //记录话题id，评论数增量，相应redis的key、value
            if (ccIncrement != null) {
                batchTopicIds.add(topicId);
                batchCcIncrements.add(ccIncrement.intValue());
            }else {
                redisUtils.removeForSet(TopicKeyConstants.SETK_RECORDING_COMMENT_COUNT_INCREMENT, Long.toString(topicId));
            }

            //日志记录变化量
            logString.append(String.format("%s评论变化量为%d ", topicId, ccIncrement));

            if (index % BATCH_SIZE == 0 || index == topicIdList.size()) {
                //数据库批量增加浏览量
                try {
                    topicSpAttrWriteService.updateCommentCountByIncrement(batchTopicIds, batchCcIncrements);
                } catch (Exception e) {
                    log.error("error:更新commentCount变化失败 ids:{} increments:{}",batchTopicIds,batchCcIncrements);
                }

                //批量移除对应值或删除对应键值，这两者顺序不可交换！
                redisUtils.removeForSet(TopicKeyConstants.SETK_RECORDING_COMMENT_COUNT_INCREMENT, batchTopicIds);

                log.debug(logString.toString());
                //清空
                batchTopicIds.clear();
                batchCcIncrements.clear();

                //重新初始化
                index=0;
                logString = new StringBuilder();
            }
        }
        log.info("end 系统定时任务：保存评论数变化");
    }
}

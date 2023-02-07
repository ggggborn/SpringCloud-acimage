package com.acimage.community.service.topic.Impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Pair;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.utils.LambdaUtils;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.common.utils.common.PairUtils;
import com.acimage.community.dao.TopicDao;
import com.acimage.community.service.topic.TopicRankWriteService;
import com.acimage.community.service.topic.TopicSpAttrQueryService;
import com.acimage.community.service.topic.TopicSpAttrWriteService;
import com.acimage.community.global.consts.TopicKeyConstants;
import com.acimage.community.global.enums.TopicAttribute;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TopicSpAttrWriteServiceImpl implements TopicSpAttrWriteService {
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    TopicDao topicDao;
    @Autowired
    TopicSpAttrQueryService topicSpAttrQueryService;
    @Autowired
    TopicRankWriteService topicRankWriteService;

    @Override
    public void removeAttributes(long topicId) {
        for (TopicAttribute attr : TopicAttribute.values()) {
            //删除保存属性最新变化的数据
            redisUtils.delete(attr.keyPrefix() + topicId);
            //删除set中记录的id
            redisUtils.removeForSet(attr.setKeyForRecordingId(), Long.toString(topicId));
            //删除排行榜中的id
            redisUtils.removeForZSet(attr.zSetKey(), topicId);
        }
    }

    @Override
    public void updateStarCountByIncrement(List<Long> topicIds, List<Integer> increments) {
        batchUpdateByIncrements(TopicAttribute.STAR_COUNT, topicIds, increments);
    }

    @Override
    public void updateCommentCountByIncrement(List<Long> topicIds, List<Integer> increments) {
        batchUpdateByIncrements(TopicAttribute.COMMENT_COUNT, topicIds, increments);
    }

    @Override
    public void updatePageViewByIncrement(List<Long> topicIds, List<Integer> increments) {
        batchUpdateByIncrements(TopicAttribute.PAGE_VIEW, topicIds, increments);
    }

    private void batchUpdateByIncrements(TopicAttribute attr, List<Long> topicIds, List<Integer> increments) {
        String underlineColumnName = attr.toUnderlineColumnName();
        List<Pair<Long, Integer>> pairList = PairUtils.combine(topicIds, increments);
        if (!CollectionUtil.isEmpty(pairList)) {
            topicDao.batchUpdateColumnByIncrement(underlineColumnName, pairList);
        }
    }

    private void updateByIncrement(SFunction<Topic, ?> column, long topicId, int increment) {
        String underlineName = LambdaUtils.getUnderlineColumnName(column);
        topicDao.updateColumnByIncrement(underlineName, topicId, increment);

    }

    @Override
    public void updateActivityTime(List<Long> topicIds, List<Date> activityTimes) {
        List<Pair<Long, Date>> pairList = PairUtils.combine(topicIds, activityTimes);
        if (!CollectionUtil.isEmpty(pairList)) {
            topicDao.batchUpdateActivityTime(pairList);
        }
    }

    @Override
    public void changeActivityTime(long topicId, Date date) {
        String key = TopicKeyConstants.STRINGKP_TOPIC_ACTIVITY_TIME + topicId;
        //保存最新值
        redisUtils.setObjectJson(key, date);
        //记录id
        redisUtils.addForSet(TopicKeyConstants.SETK_RECORDING_ACTIVITY_TIME, topicId);
        //更新排行榜
        topicRankWriteService.updateRank(TopicAttribute.ACTIVITY_TIME, topicId, date.getTime());
    }

    @Override
    public void increaseStarCount(long topicId, int increment) {
        String key = TopicKeyConstants.STRINGKP_TOPIC_STAR_COUNT_INCREMENT + topicId;
        if (increment != 0) {
            //记录增量
            redisUtils.increment(key, increment);
            //记录id
            redisUtils.addForSet(TopicKeyConstants.SETK_RECORDING_STAR_COUNT_INCREMENT, topicId);
            //更新排行榜
            int latestStarCount = topicSpAttrQueryService.getStarCount(topicId);
            topicRankWriteService.updateRank(TopicAttribute.STAR_COUNT, topicId, latestStarCount);
        }
    }

    @Override
    public void increaseCommentCount(long topicId, int increment) {
        if (increment != 0) {
            //改数据库
            this.updateByIncrement(Topic::getCommentCount, topicId, increment);
            String column = LambdaUtils.columnNameOf(Topic::getCommentCount);
            //同步到redis
            String key= TopicKeyConstants.HASHKP_TOPIC+topicId;
            Long commentCount=redisUtils.incrementIfPresentForHashKey(key, column, increment);
            if(commentCount!=null){
                topicRankWriteService.updateRank(TopicAttribute.COMMENT_COUNT, topicId, commentCount.intValue());
            }
        }
    }


}

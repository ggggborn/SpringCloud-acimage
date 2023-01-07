package com.acimage.community.service.topic.Impl;

import com.acimage.common.model.domain.Topic;
import com.acimage.common.utils.RedisUtils;
import com.acimage.community.service.topic.TopicRankWriteService;
import com.acimage.community.service.topic.consts.KeyConstants;
import com.acimage.community.service.topic.enums.TopicAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Service
public class TopicRankWriteServiceImpl implements TopicRankWriteService {
    @Autowired
    RedisUtils redisUtils;

    @Override
    public void updateRank(TopicAttribute attr, long topicId, double newScore) {

        String key = attr.zSetKey();
        if (key == null) {
            return;
        }

        //更新对应排行榜
        redisUtils.addForZSet(key, Long.toString(topicId), newScore);
    }

    @Override
    public void updateRank(TopicAttribute attr, @NotNull Topic topic) {
        if (topic == null) {
            return;
        }

        if (attr == TopicAttribute.ACTIVITY_TIME) {
            Date activityTime = redisUtils.getObjectFromString(KeyConstants.STRINGKP_TOPIC_ACTIVITY_TIME + topic.getId(), Date.class);
            if (activityTime == null) {
                activityTime = topic.getActivityTime();
            }
            //更新对应排行榜
            redisUtils.addForZSet(attr.zSetKey(), topic.getId().toString(), activityTime.getTime());

        } else if (attr == TopicAttribute.PAGE_VIEW) {
            String logKey = KeyConstants.LOGKP_TOPIC_PV + topic.getId();
            int increment = redisUtils.sizeForHyperLogLog(logKey).intValue();
            int latestPv = topic.getPageView() + increment;
            //更新对应排行榜
            redisUtils.addForZSet(attr.zSetKey(), topic.getId().toString(), latestPv);

        } else if (attr == TopicAttribute.COMMENT_COUNT || attr == TopicAttribute.STAR_COUNT) {
            String key = attr.keyPrefix() + topic.getId();
            Integer increment = redisUtils.getForString(key, Integer.class);
            int baseValue = (Integer) attr.toGetFunction().apply(topic);
            if (increment != null) {
                baseValue += increment;
            }
            //更新对应排行榜
            redisUtils.addForZSet(attr.zSetKey(), topic.getId().toString(), baseValue);

        }

    }
}

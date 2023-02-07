package com.acimage.community.service.topic.Impl;

import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.community.service.topic.TopicQueryService;
import com.acimage.community.service.topic.TopicSpAttrQueryService;
import com.acimage.community.global.consts.TopicKeyConstants;
import com.acimage.community.global.enums.TopicAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TopicSpAttrQueryServiceImpl implements TopicSpAttrQueryService {
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    TopicQueryService topicQueryService;

    @Override
    public Integer getPageView(long topicId) {
        //获取基础浏览量
        Topic topic = topicQueryService.getTopic(topicId);
        if (topic == null) {
            return null;
        }
        int pageView = topic.getPageView();

        //从redis中获取新增浏览量
        String topicPvLogKey = TopicKeyConstants.LOGKP_TOPIC_PV + topicId;
        Long pvIncrement = redisUtils.sizeForHyperLogLog(topicPvLogKey);
        int increment = pvIncrement == null ? 0 : pvIncrement.intValue();

        return pageView + increment;
    }

    @Override
    public Date getActivityTime(long topicId) {
        //先从redis获取
        Date activityTime = redisUtils.getObjectFromString(TopicKeyConstants.STRINGKP_TOPIC_ACTIVITY_TIME + topicId, Date.class);
        if (activityTime != null) {
            return activityTime;
        }

        Topic topic = topicQueryService.getTopic(topicId);
        if (topic == null) {
            return null;
        }
        return topic.getActivityTime();
    }

    @Override
    public Integer getStarCount(long topicId) {
        //获取基础浏览量
        Topic topic = topicQueryService.getTopic(topicId);
        if (topic == null) {
            return null;
        }
        int starCount = topic.getStarCount();

        //从redis中获取新增浏览量
        Long starCountIncrement = redisUtils.getForString(TopicAttribute.STAR_COUNT.keyPrefix() + topicId, Long.class);
        int increment = starCountIncrement == null ? 0 : starCountIncrement.intValue();

        return starCount + increment;
    }

    @Override
    public Integer getCommentCount(long topicId) {
        //获取基础浏览量
        Topic topic = topicQueryService.getTopic(topicId);
        if (topic == null) {
            return null;
        }
        int commentCount = topic.getCommentCount();

        //从redis中获取新增浏览量
        Long commentIncrement = redisUtils.getForString(TopicAttribute.COMMENT_COUNT.keyPrefix() + topicId, Long.class);
        int increment = commentIncrement == null ? 0 : commentIncrement.intValue();

        return commentCount + increment;
    }

    @Override
    public void setAttrIntoTopic(Topic topic, TopicAttribute... attrs) {
        if (topic == null) {
            return;
        }

        List<TopicAttribute> typeList = Arrays.asList(attrs);
        long topicId = topic.getId();

        if (typeList.contains(TopicAttribute.PAGE_VIEW)) {
            Integer pv = this.getPageView(topicId);
            topic.setPageView(pv);
//            topicSpAttrWriteService.updateRank(TopicAttribute.PAGE_VIEW,topicId,pv);
        }

        if (typeList.contains(TopicAttribute.STAR_COUNT)) {
            Integer starCount = this.getStarCount(topicId);
            topic.setStarCount(starCount);
//            topicSpAttrWriteService.updateRank(TopicAttribute.STAR_COUNT,topicId,starCount);

        }

        if (typeList.contains(TopicAttribute.COMMENT_COUNT)) {
            Integer commentCount = this.getCommentCount(topicId);
            topic.setCommentCount(commentCount);
//            topicSpAttrWriteService.updateRank(TopicAttribute.COMMENT_COUNT,topicId,commentCount);
        }

        if (typeList.contains(TopicAttribute.ACTIVITY_TIME)) {
            Date activityTime = this.getActivityTime(topicId);
            topic.setActivityTime(activityTime);
//            topicSpAttrWriteService.updateRank(TopicAttribute.ACTIVITY_TIME,topicId,activityTime.getTime());
        }
    }


}

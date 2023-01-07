package com.acimage.community.service.topic.enums;


import com.acimage.common.model.domain.Topic;
import com.acimage.common.utils.LambdaUtils;
import com.acimage.community.service.topic.consts.KeyConstants;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.*;

public enum TopicAttribute {
    STAR_COUNT,
    PAGE_VIEW,
    COMMENT_COUNT,
    ACTIVITY_TIME;
    public static final Map<TopicAttribute, String> toZSetKeyForRank;
    public static final Map<TopicAttribute, String> toSetKey;
    public static final Map<TopicAttribute, String> toKeyPrefix;
    public static final Map<TopicAttribute, SFunction<Topic,Object>> toTopicField;

    static {
        toZSetKeyForRank = new HashMap<>() {{
            put(STAR_COUNT, KeyConstants.ZSETK_TOPIC_STAR_COUNT);
            put(PAGE_VIEW, KeyConstants.ZSETK_TOPIC_PV);
            put(COMMENT_COUNT, KeyConstants.ZSETK_TOPIC_COMMENT_COUNT);
            put(ACTIVITY_TIME, KeyConstants.ZSETK_TOPIC_ACTIVITY_TIME);
        }};

        toSetKey = new HashMap<>() {{
            put(STAR_COUNT, KeyConstants.SETK_RECORDING_STAR_COUNT_INCREMENT);
            put(PAGE_VIEW, KeyConstants.SETK_RECORDING_PV_INCREMENT);
            put(COMMENT_COUNT, KeyConstants.SETK_RECORDING_COMMENT_COUNT_INCREMENT);
            put(ACTIVITY_TIME, KeyConstants.SETK_RECORDING_ACTIVITY_TIME);
        }};

        toKeyPrefix = new HashMap<>() {{
            put(STAR_COUNT, KeyConstants.STRINGKP_TOPIC_STAR_COUNT_INCREMENT);
            put(PAGE_VIEW, KeyConstants.LOGKP_TOPIC_PV);
            put(COMMENT_COUNT, KeyConstants.STRINGKP_TOPIC_COMMENT_COUNT_INCREMENT);
            put(ACTIVITY_TIME, KeyConstants.STRINGKP_TOPIC_ACTIVITY_TIME);
        }};

        toTopicField = new HashMap<>() {{
            put(STAR_COUNT, Topic::getStarCount);
            put(PAGE_VIEW, Topic::getPageView);
            put(COMMENT_COUNT, Topic::getCommentCount);
            put(ACTIVITY_TIME, Topic::getActivityTime);
        }};
    }

    public String zSetKey() {
        return toZSetKeyForRank.get(this);
    }

    public String setKeyForRecordingId() {
        return toSetKey.get(this);
    }

    public String keyPrefix() {
        return toKeyPrefix.get(this);
    }

    public String toUnderlineColumnName() {
        return LambdaUtils.getUnderlineColumnName(toTopicField.get(this));
    }

    public String toFieldName() {
        return LambdaUtils.getCamelColumnName(toTopicField.get(this));
    }

    public SFunction<Topic,Object> toGetFunction(){
        return toTopicField.get(this);
    }

}

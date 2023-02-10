package com.acimage.community.global.consts;

public class TopicKeyConstants {
    public static final String HASHKP_TOPIC = "acimage:topics:id:";
    public static final String HASHKP_TOPIC_HTML="acimage:community:topicHtml:topicId:";


    public static final String STRINGKP_TOPIC_STAR_COUNT_INCREMENT = "acimage:topics:starCountIncrement:id:";
    public static final String STRINGKP_TOPIC_COMMENT_COUNT_INCREMENT = "acimage:topics:commentCountIncrement:id:";
    public static final String STRINGKP_TOPIC_ACTIVITY_TIME = "acimage:topics:activityTime:id:";
    /**
     * key前缀，统计浏览了对应话题ip数，redis类型：HyperLogLog
     */
    public static final String LOGKP_TOPIC_PV ="acimage:topics:pageViewLog:id:";



    /**
     * key，记录所有被记录浏览量增量的话题id， redis类型：set
     */
    public static final String SETK_RECORDING_PV_INCREMENT ="acimage:topics:recording:pageViewLog";
    /**
     * key，记录所有被记录收藏量增量的话题id， redis类型：set
     */
    public static final String SETK_RECORDING_STAR_COUNT_INCREMENT ="acimage:topics:recording:starCountIncrement";
    /**
     * key，记录所有被记录评论数增量的话题id， redis类型：set
     */
    public static final String SETK_RECORDING_COMMENT_COUNT_INCREMENT ="acimage:topics:recording:commentCountIncrement";
    public static final String SETK_RECORDING_ACTIVITY_TIME ="acimage:topics:recording:activityTime";


    public static final String ZSETK_TOPIC_STAR_COUNT ="acimage:topics:rank:starCount";
    public static final String ZSETK_TOPIC_PV ="acimage:topics:rank:pageView";
    /**
     * key前缀，记录话题的最新变化时间（如新增评论等）
     */
    public static final String ZSETK_TOPIC_ACTIVITY_TIME ="acimage:topics:rank:activityTime";
    public static final String ZSETK_TOPIC_COMMENT_COUNT ="acimage:topics:rank:commentCount";

}

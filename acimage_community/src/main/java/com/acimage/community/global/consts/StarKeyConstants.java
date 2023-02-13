package com.acimage.community.global.consts;

public class StarKeyConstants {
    public static final String STRINGKP_TOPIC_STAR_COUNT = "acimage:community:stars:starCount:topicId:";
    public static final String STRINGKP_USER_STAR_COUNT = "acimage:community:stars:starCount:userId:";
    public static final String STRINGKP_STAR_USER_TOPIC = "acimage:community:stars:isStar:userId:topicId:";

    public static String keyOfIsStar(long userId, long topicId) {
        return STRINGKP_STAR_USER_TOPIC + userId + ":" + topicId;
    }
}

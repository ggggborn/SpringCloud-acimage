package com.acimage.community.global.consts;

public class CommentKeyConstants {
    public static final String STRINGKP_COMMENT_COUNT = "acimage:comments:commentCount:topicId:";
    public static final String STRINGKP_TOPIC_COMMENTS = "acimage:comments:topicId:pageNo:";
    public static final String STRINGKP_USER_COMMENTS = "acimage:comments:userId:pageNo:";

    public static String keyOfTopicComments(long topicId, int pageNo) {
        return String.format("%s%s:%s", STRINGKP_TOPIC_COMMENTS, topicId, pageNo);
    }
}

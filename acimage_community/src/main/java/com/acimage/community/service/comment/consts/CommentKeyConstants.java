package com.acimage.community.service.comment.consts;

public class CommentKeyConstants {
    public static final String STRINGKP_COMMENT_COUNT = "acimage:comments:commentCount:topicId:";
    public static final String STRINGKP_TOPIC_COMMENTS = "acimage:comments:topicId:pageNo:";

    public static String keyOfTopicComments(long topicId, int pageNo){
        return STRINGKP_TOPIC_COMMENTS+topicId+":"+pageNo;
    }
}

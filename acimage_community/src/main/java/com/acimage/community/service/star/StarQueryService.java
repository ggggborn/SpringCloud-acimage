package com.acimage.community.service.star;

public interface StarQueryService {
    boolean isStar(long userId,long topicId);

    Integer getTopicStarCount(long topicId);

    Integer getStarCountOwnedBy(long userId);
}

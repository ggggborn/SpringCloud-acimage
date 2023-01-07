package com.acimage.community.service.topic;

import com.acimage.community.service.topic.enums.TopicAttribute;

import java.util.concurrent.TimeUnit;

public interface TopicPreheatService {
    void preheatTopicsOrderBy(TopicAttribute attr, int sizeOfLoadIntoRank, int numberOfCacheTopics, long timeout, TimeUnit timeUnit);
}

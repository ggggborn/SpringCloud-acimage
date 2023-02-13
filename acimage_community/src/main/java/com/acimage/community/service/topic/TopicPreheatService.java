package com.acimage.community.service.topic;

import com.acimage.community.global.enums.TopicAttribute;

import java.util.concurrent.TimeUnit;

public interface TopicPreheatService {
    void preheatTopicsOrderBy(TopicAttribute attr, int rankSize, int cacheSize, long timeout, TimeUnit timeUnit);
}

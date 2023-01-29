package com.acimage.community.service.tag;

import java.util.List;

public interface TagTopicQueryService {
    List<Integer> listTagIds(long topicId);
}

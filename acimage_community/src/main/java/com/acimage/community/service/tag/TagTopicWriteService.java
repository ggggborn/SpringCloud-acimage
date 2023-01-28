package com.acimage.community.service.tag;

import java.util.List;

public interface TagTopicWriteService {
    void save(long topicId, List<Integer> tagIdList);

    void remove(long topicId);
}

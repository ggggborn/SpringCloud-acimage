package com.acimage.community.service.topic;

import com.acimage.common.model.domain.Topic;
import com.acimage.common.model.page.Page;

import java.util.List;

public interface TopicInfoQueryService {
    Topic getTopicWithUser(long topicId);

    Topic getTopicInfoAndFirstCommentPage(long topicId);

    Page<Topic> pageTopicsInfoOrderByCreateTime(long userId, int pageNo,int pageSize);

    List<Topic> pageTopicsInfoRankByPageView(int pageNo, int pageSize);

    List<Topic> pageTopicsInfoRankByStarCount(int pageNo,int pageSize);

    Page<Topic> pageTopicsInfoRankByActivityTime(int pageNo,int pageSize);
}

package com.acimage.community.service.topic;

import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.page.Page;
import com.acimage.community.model.vo.TopicInfoVo;

import java.util.List;

public interface TopicInfoQueryService {
    Topic getTopicWithUserTagIds(long topicId);

    TopicInfoVo getTopicInfoAndFirstCommentPage(long topicId);

    Page<Topic> pageTopicsInfoOrderByCreateTime(long userId, int pageNo,int pageSize);

    List<Topic> pageTopicsInfoRankByPageView(int pageNo, int pageSize);

    List<Topic> pageTopicsInfoRankByStarCount(int pageNo,int pageSize);

    Page<Topic> pageTopicsInfoRankByActivityTime(int pageNo,int pageSize);
}

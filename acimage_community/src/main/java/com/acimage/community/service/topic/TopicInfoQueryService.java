package com.acimage.community.service.topic;

import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.page.MyPage;
import com.acimage.community.global.enums.TopicAttribute;
import com.acimage.community.model.vo.TopicInfoVo;

import java.util.List;

public interface TopicInfoQueryService {
    Topic getTopicWithUserTagIds(long topicId);
    TopicInfoVo getTopicInfoAndFirstCommentPage(long topicId);
    MyPage<Topic> pageUserTopicsInfoOrderByCreateTime(long userId, int pageNo, int pageSize);
    List<Topic> listTopicsInfoSortBy(TopicAttribute attribute, int pageNo, int pageSize);

    List<Topic> listRandomTopicsInRank(int size);

    MyPage<Topic> pageTopicsInfoRankByActivityTime(int pageNo, int pageSize);
}

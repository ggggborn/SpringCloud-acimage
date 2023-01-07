package com.acimage.community.service.topic;

import cn.hutool.core.lang.Pair;
import com.acimage.community.service.topic.enums.TopicAttribute;

import java.util.List;


/**
 * 负责话题的 浏览量、收藏数、评论数、最新活跃时间 相关的服务
 */
public interface TopicRankQueryService {

    List<Long> pageTopicIdsInRank(TopicAttribute topicAttribute, int pageNo, int pageSize);
    List<Pair<Long, Double>> pageTopicIdWithScoresInRank(TopicAttribute topicAttribute, int pageNo, int pageSize);
    Integer countTopicIdsInRank(TopicAttribute topicAttribute);

}

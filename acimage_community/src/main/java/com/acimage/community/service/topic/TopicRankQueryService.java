package com.acimage.community.service.topic;

import cn.hutool.core.lang.Pair;
import com.acimage.community.global.enums.TopicAttribute;

import java.util.List;


/**
 * 负责话题的 浏览量、收藏数、评论数、最新活跃时间 相关的服务
 */
public interface TopicRankQueryService {

    List<Long> listTopicIdsInRank(TopicAttribute topicAttribute, int pageNo, int pageSize);

    List<Long> listRandomTopicIdsInRank(TopicAttribute topicAttribute, int size);

    List<Pair<Long, Double>> listTopicIdWithScoresInRank(TopicAttribute topicAttribute, int pageNo, int pageSize);
    Integer countTopicIdsInRank(TopicAttribute topicAttribute);

}

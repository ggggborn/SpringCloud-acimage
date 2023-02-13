package com.acimage.community.service.topic.Impl;

import cn.hutool.core.lang.Pair;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.common.utils.common.ListUtils;
import com.acimage.common.utils.common.PageUtils;
import com.acimage.community.service.topic.TopicRankQueryService;
import com.acimage.community.global.enums.TopicAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicRankQueryServiceImpl implements TopicRankQueryService {
    @Autowired
    RedisUtils redisUtils;

    @Override
    public List<Long> listTopicIdsInRank(TopicAttribute topicAttribute, int pageNo, int pageSize) {
        List<Pair<Long, Double>> topicIdAndScores = listTopicIdWithScoresInRank(topicAttribute, pageNo, pageSize);
        return ListUtils.extractKeyFrom(topicIdAndScores);
    }

    @Override
    public List<Long> listRandomTopicIdsInRank(TopicAttribute topicAttribute, int size) {
        return redisUtils.randomMembersForZSet(topicAttribute.zSetKey(),size)
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pair<Long, Double>> listTopicIdWithScoresInRank(TopicAttribute topicAttribute, int pageNo, int pageSize) {
        int startIndex = PageUtils.startIndexOf(pageNo, pageSize);
        int endIndex = PageUtils.endIndexOf(pageNo, pageSize);
        String zSetKey = topicAttribute.zSetKey();

        if (zSetKey == null) {
            return null;
        }
        List<Pair<String, Double>> topicIdStringAndScores = redisUtils.reverseRangeWithScoreForZSet(zSetKey, startIndex, endIndex);
        return ListUtils.convertToLongDoublePairFrom(topicIdStringAndScores);
    }

    @Override
    public Integer countTopicIdsInRank(@NotNull TopicAttribute topicAttribute) {
        String rankingListKey = topicAttribute.zSetKey();
        Long count = redisUtils.sizeForZSet(rankingListKey);
        if (count == null) {
            return null;
        } else {
            return count.intValue();
        }
    }




}

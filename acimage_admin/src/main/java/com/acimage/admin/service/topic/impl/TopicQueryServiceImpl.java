package com.acimage.admin.service.topic.impl;

import com.acimage.admin.dao.community.TopicDao;
import com.acimage.admin.global.consts.ModuleConstants;
import com.acimage.admin.model.request.TopicQueryReq;
import com.acimage.admin.service.topic.TopicQueryService;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.page.Page;
import com.acimage.common.utils.common.PageUtils;
import com.acimage.common.utils.redis.RedisUtils;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@DS(ModuleConstants.COMMUNITY)
@Service
public class TopicQueryServiceImpl implements TopicQueryService {
    public static final String STRINGKP_TOPIC_COUNT = "acimage:admin:topic:totalCount:";
    @Autowired
    TopicDao topicDao;
    @Autowired
    RedisUtils redisUtils;

    @Override
    public Page<Topic> listOrderByColumn(TopicQueryReq topicQueryReq) {
        int pageNo = topicQueryReq.getPageNo();
        int pageSize = topicQueryReq.getPageSize();
        String column = StringUtils.camelToUnderline(topicQueryReq.getColumn()) ;
        int startIndex = PageUtils.startIndexOf(pageNo, pageSize);

        QueryWrapper<Topic> qw = new QueryWrapper<>();
        qw.orderByDesc(column).last(String.format("limit %d,%d", startIndex, pageSize));
        List<Topic> topicList = topicDao.selectList(qw);
        int count = this.getTopicCount();

        return new Page<>(count, topicList);
    }

    @Override
    public Integer getTopicCount() {
        Integer totalCount = redisUtils.getForString(STRINGKP_TOPIC_COUNT, Integer.class);
        if (totalCount != null) {
            return totalCount;
        }
        LambdaQueryWrapper<Topic> qw = new LambdaQueryWrapper<>();
        qw.select(Topic::getId);
        totalCount = topicDao.selectCount(qw);
        long timeout = 2L;
        redisUtils.setAsString(STRINGKP_TOPIC_COUNT, totalCount.toString(), timeout, TimeUnit.MINUTES);
        return totalCount;
    }
}

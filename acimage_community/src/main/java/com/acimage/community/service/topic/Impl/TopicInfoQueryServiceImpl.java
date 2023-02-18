package com.acimage.community.service.topic.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.acimage.common.global.exception.BusinessException;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.model.domain.community.CmtyUser;
import com.acimage.common.model.domain.community.Comment;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.domain.user.User;
import com.acimage.common.redis.annotation.QueryRedis;
import com.acimage.common.utils.LambdaUtils;
import com.acimage.common.utils.common.BeanUtils;
import com.acimage.common.utils.common.PageUtils;
import com.acimage.community.dao.TopicDao;
import com.acimage.common.model.page.MyPage;
import com.acimage.community.global.consts.PageSizeConstants;
import com.acimage.community.model.vo.TopicInfoVo;
import com.acimage.community.service.cmtyuser.CmtyUserQueryService;
import com.acimage.community.service.comment.CommentInfoQueryService;
import com.acimage.community.service.tag.TagTopicQueryService;
import com.acimage.community.service.topic.*;
import com.acimage.community.global.enums.TopicAttribute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TopicInfoQueryServiceImpl implements TopicInfoQueryService {
    @Autowired
    TopicDao topicDao;
    @Autowired
    CommentInfoQueryService commentInfoQueryService;
    @Autowired
    TopicQueryService topicQueryService;
    @Autowired
    TopicSpAttrQueryService topicSpQueryService;
    @Autowired
    TopicRankQueryService topicRankQueryService;
    @Autowired
    TopicEsSearchService topicEsSearchService;
    @Autowired
    CmtyUserQueryService cmtyUserQueryService;
    @Autowired
    TagTopicQueryService tagTopicQueryService;
    @Autowired
    TopicHtmlQueryService topicHtmlQueryService;


    @Override
    public Topic getTopicWithUserTagIds(long topicId) {
        Topic topic = topicQueryService.getTopic(topicId);
        if (topic == null) {
            return null;
        }
        List<Integer> tagIds = tagTopicQueryService.listTagIds(topicId);
        topic.setTagIds(tagIds);
        return topic;
    }

    @Override
    public TopicInfoVo getTopicInfoAndFirstCommentPage(long topicId) {

        Topic topic = this.getTopicWithUserTagIds(topicId);

        if (topic == null) {
            log.warn("user:{} 查询 话题:{} error：不存在或已被删除", UserContext.getUsername(), topicId);
            throw new BusinessException("话题不存在或已被删除");
        }
        TopicInfoVo topicInfoVo = new TopicInfoVo();
        //设置浏览量、收藏量、评论数
        topicSpQueryService.setAttrIntoTopic(topic, TopicAttribute.COMMENT_COUNT, TopicAttribute.STAR_COUNT, TopicAttribute.PAGE_VIEW);

        BeanUtil.copyProperties(topic, topicInfoVo, false);
        //查找首页评论
        int pageNo = 1;
        List<Comment> comments = commentInfoQueryService.pageCommentsWithUser(topicId, pageNo, PageSizeConstants.TOPIC_COMMENTS);
        topicInfoVo.setComments(comments);

        CmtyUser cmtyUser = cmtyUserQueryService.getCmtyUser(topic.getUserId());

        User user = BeanUtils.copyPropertiesTo(cmtyUser, User.class);
        topicInfoVo.setUser(user);
        String html = topicHtmlQueryService.getTopicHtml(topicId).getHtml();
        topicInfoVo.setHtml(html);
        topicInfoVo.setSimilarTopics(topicEsSearchService.searchSimilarByTitle(topicId, topicInfoVo.getTitle(), 10));

        return topicInfoVo;
    }

    @QueryRedis(keyPrefix = "acimage:community:topics:userId:",expire = 8L,unit = TimeUnit.SECONDS)
    @Override
    public MyPage<Topic> pageUserTopicsInfoOrderByCreateTime(long userId, int pageNo, int pageSize) {
        int starIndex = PageUtils.startIndexOf(pageNo, pageSize);
        List<Topic> topics = topicDao.selectTopicsWithUserOrderByCreateTime(userId, starIndex, pageSize);
        return new MyPage<>(topicDao.countTopics(userId), topics);
    }

    @Override
    public List<Topic> listTopicsInfoSortBy(TopicAttribute attr, int pageNo, int pageSize) {

        List<Long> rankList = topicRankQueryService.listTopicIdsInRank(attr, pageNo, pageSize);
        List<Topic> topicList = new ArrayList<>();

        if (pageNo == 1 && rankList.size() < pageSize) {
            String column = attr.toFieldName();
            topicList = topicDao.selectTopicsWithUserOrderBy(column, pageSize);
        } else {
            for (Long topicId : rankList) {
                Topic topic = getTopicWithUserTagIds(topicId);
                if (topic != null) {
                    topicList.add(topic);
                }
            }
        }

        for (Topic topic : topicList) {
            //设置浏览量
            topicSpQueryService.setAttrIntoTopic(topic, TopicAttribute.PAGE_VIEW);
        }

        return topicList;
    }

    @Override
    public List<Topic> listRandomTopicsInRank(int size) {
        //获取随机属性
        TopicAttribute[] attrs = TopicAttribute.values();
        int len = attrs.length;
        int i = (int) (System.currentTimeMillis() % len);
        TopicAttribute attr=attrs[i];
        //从随机属性排行中获取随机话题
        List<Long> rankList = topicRankQueryService.listRandomTopicIdsInRank(attr, size);
        List<Topic> topicList = new ArrayList<>();
        for (Long topicId : rankList) {
            Topic topic = getTopicWithUserTagIds(topicId);
            if (topic != null) {
                topicList.add(topic);
            }
        }

        for (Topic topic : topicList) {
            //设置浏览量
            topicSpQueryService.setAttrIntoTopic(topic, TopicAttribute.PAGE_VIEW);
        }
        return topicList;

    }

    @Override
    public MyPage<Topic> pageTopicsInfoRankByActivityTime(int pageNo, int pageSize) {
        List<Long> topicIdList = topicRankQueryService.listTopicIdsInRank(TopicAttribute.ACTIVITY_TIME, pageNo, pageSize);
        List<Topic> topicList = new ArrayList<>();
        for (Long topicId : topicIdList) {
            Topic topic = getTopicWithUserTagIds(topicId);
            if (topic != null) {
                //设置浏览量
                topicSpQueryService.setAttrIntoTopic(topic, TopicAttribute.PAGE_VIEW);
                topicList.add(topic);
            }
        }
        return new MyPage<>(topicRankQueryService.countTopicIdsInRank(TopicAttribute.ACTIVITY_TIME), topicList);
    }
}

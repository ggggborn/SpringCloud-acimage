package com.acimage.community.service.topic.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.acimage.common.exception.BusinessException;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.model.domain.community.Comment;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.domain.user.User;
import com.acimage.common.model.domain.community.UserCommunityStatistic;
import com.acimage.common.utils.LambdaUtils;
import com.acimage.common.utils.common.PageUtils;
import com.acimage.community.dao.TopicDao;
import com.acimage.common.model.page.Page;
import com.acimage.community.model.vo.TopicInfoVo;
import com.acimage.community.service.comment.CommentInfoService;
import com.acimage.community.service.tag.TagTopicQueryService;
import com.acimage.community.service.topic.*;
import com.acimage.community.service.topic.enums.TopicAttribute;
import com.acimage.community.service.userstatistic.UserCsQueryService;
import com.acimage.feign.client.ImageClient;
import com.acimage.feign.client.UserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class TopicInfoQueryServiceImpl implements TopicInfoQueryService {
    @Autowired
    TopicDao topicDao;

    @Autowired
    CommentInfoService commentInfoService;
    @Autowired
    TopicQueryService topicQueryService;
    @Autowired
    TopicSpAttrQueryService topicSpQueryService;
    @Autowired
    TopicRankQueryService topicRankQueryService;
    @Autowired
    UserCsQueryService userCsQueryService;
    @Autowired
    TagTopicQueryService tagTopicQueryService;
    @Autowired
    ImageClient imageClient;
    @Autowired
    UserClient userClient;
    @Autowired
    TopicHtmlQueryService topicHtmlQueryService;


    @Override
    public Topic getTopicWithUserTagIds(long topicId) {
        Topic topic = topicQueryService.getTopic(topicId);
        if(topic==null){
            return null;
        }
        List<Integer> tagIds=tagTopicQueryService.listTagIds(topicId);
        topic.setTagIds(tagIds);
        return topic;
    }

    @Override
    public TopicInfoVo getTopicInfoAndFirstCommentPage(long topicId) {

        Topic topic = topicQueryService.getTopic(topicId);

        if (topic == null) {
            log.error("user:{} 查询 话题:{} error：不存在或已被删除", UserContext.getUsername(), topicId);
            throw new BusinessException("话题不存在或已被删除");
        }
        TopicInfoVo topicInfoVo=new TopicInfoVo();
        //设置浏览量、收藏量、评论数
        topicSpQueryService.setAttrIntoTopic(topic, TopicAttribute.COMMENT_COUNT, TopicAttribute.STAR_COUNT, TopicAttribute.PAGE_VIEW);

        BeanUtil.copyProperties(topic,topicInfoVo,false);
        //查找首页评论
        int pageNo = 1;
        List<Comment> comments = commentInfoService.pageCommentsWithUser(topicId, pageNo);
        topicInfoVo.setComments(comments);

        User user = userClient.queryUser(topic.getUserId()).getData();
        //设置用户主人相关信息
        if (user.getId() != null) {
            UserCommunityStatistic userStatistic = userCsQueryService.getUserCommunityStatistic(topic.getUserId());
            user.setStarCount(userStatistic.getStarCount());
            user.setTopicCount(userStatistic.getTopicCount());
        }
        topicInfoVo.setUser(user);
        String html=topicHtmlQueryService.getTopicHtml(topicId).getHtml();
        topicInfoVo.setHtml(html);

//        List<Image> imageList = imageClient.queryTopicImages(topic.getId()).getData();
//        topic.setImages(imageList);

        return topicInfoVo;
    }

    @Override
    public Page<Topic> pageTopicsInfoOrderByCreateTime(long userId, int pageNo, int pageSize) {
        int starIndex = PageUtils.startIndexOf(pageNo, pageSize);
        List<Topic> topics = topicDao.selectTopicsWithUserImagesOrderByCreateTime(userId, starIndex, pageSize);
        return new Page<>(topicDao.countTopics(userId), topics);
    }

    @Override
    public List<Topic> pageTopicsInfoRankByPageView(int pageNo, int pageSize) {

        List<Long> rankList = topicRankQueryService.pageTopicIdsInRank(TopicAttribute.PAGE_VIEW, pageNo, pageSize);
        List<Topic> topicList = new ArrayList<>();

        if (pageNo == 1 && rankList.size() < pageSize) {
            //数据数不够则从数据库查
//            int dayOffset = -300;
//            String dateFormat = "yyyy-MM-dd HH:mm:ss";
//            //获取统计开始时间
//            Date beginOfDay = DateUtil.beginOfDay(new Date());
//            Date startDate = DateUtil.offsetDay(beginOfDay, dayOffset);
//            String startTime = DateUtil.format(startDate, dateFormat);
            String column = LambdaUtils.getUnderlineColumnName(Topic::getPageView);
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
            //设置浏览量,评论数，收藏量
            topicSpQueryService.setAttrIntoTopic(topic,
                    TopicAttribute.ACTIVITY_TIME,
                    TopicAttribute.PAGE_VIEW,
                    TopicAttribute.STAR_COUNT,
                    TopicAttribute.COMMENT_COUNT);
        }

        topicList.sort(Comparator.comparing(Topic::getPageView).reversed());
        return topicList;

    }

    @Override
    public List<Topic> pageTopicsInfoRankByStarCount(int pageNo, int pageSize) {

        List<Long> rankList = topicRankQueryService.pageTopicIdsInRank(TopicAttribute.STAR_COUNT, pageNo, pageSize);
        List<Topic> topicList = new ArrayList<>();
        for (Long topicId : rankList) {
            Topic topic = getTopicWithUserTagIds(topicId);
            if (topic != null) {
                //设置浏览量,评论数，收藏量
                topicSpQueryService.setAttrIntoTopic(topic,
                        TopicAttribute.ACTIVITY_TIME,
                        TopicAttribute.PAGE_VIEW,
                        TopicAttribute.STAR_COUNT,
                        TopicAttribute.COMMENT_COUNT);
                topicList.add(topic);
            }
        }

        topicList.sort(Comparator.comparing(Topic::getStarCount).reversed());
        return topicList;
    }

    @Override
    public Page<Topic> pageTopicsInfoRankByActivityTime(int pageNo, int pageSize) {
        List<Long> topicIdList = topicRankQueryService.pageTopicIdsInRank(TopicAttribute.ACTIVITY_TIME, pageNo, pageSize);
        List<Topic> topicList = new ArrayList<>();
        for (Long topicId : topicIdList) {
            Topic topic = getTopicWithUserTagIds(topicId);
            if (topic != null) {
                //设置浏览量,评论数，收藏量
                topicSpQueryService.setAttrIntoTopic(topic,
                        TopicAttribute.ACTIVITY_TIME,
                        TopicAttribute.PAGE_VIEW,
                        TopicAttribute.STAR_COUNT,
                        TopicAttribute.COMMENT_COUNT);
                topicList.add(topic);
            }
        }

        topicList.sort(Comparator.comparing(Topic::getActivityTime).reversed());
        return new Page<>(topicRankQueryService.countTopicIdsInRank(TopicAttribute.ACTIVITY_TIME), topicList);
    }
}

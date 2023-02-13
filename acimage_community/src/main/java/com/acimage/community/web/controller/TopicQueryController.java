package com.acimage.community.web.controller;


import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.page.MyPage;
import com.acimage.common.redis.annotation.RequestLimit;
import com.acimage.common.redis.enums.LimitTarget;
import com.acimage.common.result.Result;
import com.acimage.common.deprecated.annotation.Authentication;
import com.acimage.community.global.annotation.TopicId;
import com.acimage.community.global.consts.PageSizeConstants;
import com.acimage.community.global.enums.TopicAttribute;
import com.acimage.community.model.request.TopicQueryByCategoryIdReq;
import com.acimage.community.model.request.TopicQueryBySortReq;
import com.acimage.community.model.request.TopicQueryByTagIdReq;
import com.acimage.community.model.vo.TopicInfoVo;
import com.acimage.community.service.topic.TopicEsSearchService;
import com.acimage.community.service.topic.TopicInfoQueryService;
import com.acimage.community.global.annotation.RecordPageView;
import com.acimage.common.global.context.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;


@RestController
@Slf4j
@RequestMapping("/api/community/topics/query")
@Validated
@Authentication
public class TopicQueryController {
    @Autowired
    TopicInfoQueryService topicInfoQueryService;
    @Autowired
    TopicEsSearchService topicEsSearchService;

    @RequestLimit(limitTimes = {15}, durations = {5}, penaltyTimes = {-1}, targets = {LimitTarget.IP})
    @RecordPageView
    @GetMapping("/info/{id}")
    public Result<TopicInfoVo> queryTopicAndFirstCommentPage(@TopicId @Positive @PathVariable("id") Long id) {
        TopicInfoVo topicInfoVo = topicInfoQueryService.getTopicInfoAndFirstCommentPage(id);
        return Result.ok(topicInfoVo);
    }

    @GetMapping("/byTagId")
    public Result<MyPage<Topic>> pageByTagId(@Validated @ModelAttribute TopicQueryByTagIdReq queryReq) {
        return Result.ok(topicEsSearchService.searchByTagId(queryReq));
    }

    @GetMapping("/byCategoryId")
    public Result<MyPage<Topic>> pageByCategoryId(@Validated @ModelAttribute TopicQueryByCategoryIdReq queryReq) {
        return Result.ok(topicEsSearchService.searchBySort(queryReq));
    }

    @GetMapping("/bySort")
    public Result<MyPage<Topic>> pageTopicsBySort(@Validated @ModelAttribute TopicQueryBySortReq queryReq) {
        return Result.ok(topicEsSearchService.searchBySort(queryReq));
    }

    /**
     * @return 三个列表，分别是最多评论，最多star，最多浏览的评论
     */
    @GetMapping("/hot/3attrs")
    public Result<List<List<Topic>>> combineHotTopics() {
        List<List<Topic>> list=new ArrayList<>();
        list.add(topicInfoQueryService.listTopicsInfoSortBy(TopicAttribute.COMMENT_COUNT,1,10));
        list.add(topicInfoQueryService.listTopicsInfoSortBy(TopicAttribute.STAR_COUNT,1,10));
        list.add(topicInfoQueryService.listTopicsInfoSortBy(TopicAttribute.PAGE_VIEW,1,10));
        return Result.ok(list);
    }

    @RequestLimit(limitTimes = {15}, durations = {5}, penaltyTimes = {-1}, targets = {LimitTarget.IP})
    @GetMapping("/recentHot")
    public Result<List<Topic>> queryRecentHotTopics() {
        int pageNo = 1;
        int pageSize=10;
        return Result.ok(topicInfoQueryService.listTopicsInfoSortBy(TopicAttribute.PAGE_VIEW,pageNo,pageSize));
    }

    @RequestLimit(limitTimes = {15}, durations = {5}, penaltyTimes = {-1}, targets = {LimitTarget.IP})
    @GetMapping("/recommend")
    public Result<List<Topic>> queryRecommendedTopics() {
        int size=4;
        return Result.ok(topicInfoQueryService.listRandomTopicsInRank(size));
    }

    @RequestLimit(limitTimes = {15}, durations = {5}, penaltyTimes = {-1}, targets = {LimitTarget.IP})
    @GetMapping("/most/commentCount")
    public Result<List<Topic>> queryMostCommentCountTopics() {
        int pageNo = 1;
        int pageSize=10;
        return Result.ok(topicInfoQueryService.listTopicsInfoSortBy(TopicAttribute.COMMENT_COUNT,pageNo,pageSize));
    }

    @RequestLimit(limitTimes = {15}, durations = {5}, penaltyTimes = {-1}, targets = {LimitTarget.IP})
    @GetMapping("/most/active/{pageNo}/{pageSize}")
    public Result<MyPage<Topic>> pageActiveTopics(@Range(min = 1, max = 100, message = "页码在1到100之间") @PathVariable int pageNo,
                                                  @Range(min = 4, max = 20, message = "页大小在4到20之间") @PathVariable int pageSize) {
        return Result.ok(topicInfoQueryService.pageTopicsInfoRankByActivityTime(pageNo, pageSize));
    }

    @RequestLimit(limitTimes = {15}, durations = {5}, penaltyTimes = {-1}, targets = {LimitTarget.IP})
    @GetMapping("/mine/{pageNo}")
    public Result<MyPage<Topic>> queryMyTopics(@Range(min = 1, max = 100, message = "页码在1到100之间") @PathVariable("pageNo") int pageNo) {
        return Result.ok(topicInfoQueryService.pageUserTopicsInfoOrderByCreateTime(UserContext.getUserId(), pageNo, PageSizeConstants.ACTIVITY_TOPICS));
    }
}

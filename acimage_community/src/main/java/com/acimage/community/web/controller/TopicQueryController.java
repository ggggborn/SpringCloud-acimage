package com.acimage.community.web.controller;


import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.page.MyPage;
import com.acimage.common.redis.annotation.RequestLimit;
import com.acimage.common.redis.enums.LimitTarget;
import com.acimage.common.result.Result;
import com.acimage.common.deprecated.annotation.Authentication;
import com.acimage.community.global.annotation.TopicId;
import com.acimage.community.global.consts.PageSizeConstants;
import com.acimage.community.model.request.TopicQueryByCategoryIdReq;
import com.acimage.community.model.request.TopicQueryByTagIdReq;
import com.acimage.community.model.vo.TopicInfoVo;
import com.acimage.community.service.topic.TopicEsSearchService;
import com.acimage.community.service.topic.TopicInfoQueryService;
import com.acimage.community.global.annotation.RecordPageView;
import com.acimage.common.global.context.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
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

    @RequestLimit(limitTimes = {15},durations = {5},penaltyTimes = {-1},targets = {LimitTarget.IP})
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
        return Result.ok(topicEsSearchService.searchByCategoryId(queryReq));
    }

    @RequestLimit(limitTimes = {15},durations = {5},penaltyTimes = {-1},targets = {LimitTarget.IP})
    @GetMapping("/recentHot")
    public Result<List<Topic>> queryRecentHotTopics() {
        int rankEnd = 8;
        int pageNo = 1;
        return Result.ok(topicInfoQueryService.pageTopicsInfoRankByPageView(pageNo, rankEnd));
    }

    @RequestLimit(limitTimes = {15},durations = {5},penaltyTimes = {-1},targets = {LimitTarget.IP})
    @GetMapping("/most/commentCount")
    public Result<List<Topic>> queryMostCommentCountTopics() {
        int rankEnd = 10;
        int pageNo = 1;
        return Result.ok(topicInfoQueryService.pageTopicsInfoRankByCommentCount(pageNo, rankEnd));
    }

    @RequestLimit(limitTimes = {15},durations = {5},penaltyTimes = {-1},targets = {LimitTarget.IP})
    @GetMapping("/recommend")
    public Result<List<Topic>> queryRecommendedTopics() {
        int rankEnd = 4;
        int pageNo = 1;
        return Result.ok(topicInfoQueryService.pageTopicsInfoRankByStarCount(pageNo, rankEnd));
    }

    @RequestLimit(limitTimes = {15},durations = {5},penaltyTimes = {-1},targets = {LimitTarget.IP})
    @GetMapping("/pageRecentTopics/{pageNo}")
    public Result<MyPage<Topic>> pageActiveTopics(@Positive @NotNull @PathVariable int pageNo) {
        return Result.ok(topicInfoQueryService.pageTopicsInfoRankByActivityTime(pageNo, PageSizeConstants.FORUM_TOPICS));
    }

    @RequestLimit(limitTimes = {15},durations = {5},penaltyTimes = {-1},targets = {LimitTarget.IP})
    @GetMapping("/mine/{pageNo}")
    public Result<MyPage<Topic>> queryMyTopics(@Positive @PathVariable("pageNo") int pageNo) {
        return Result.ok(topicInfoQueryService.pageTopicsInfoOrderByCreateTime(UserContext.getUserId(), pageNo, PageSizeConstants.ACTIVITY_TOPICS));
    }
}

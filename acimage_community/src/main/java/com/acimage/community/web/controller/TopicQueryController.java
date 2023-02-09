package com.acimage.community.web.controller;


import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.page.MyPage;
import com.acimage.common.redis.annotation.RequestLimit;
import com.acimage.common.redis.enums.LimitTarget;
import com.acimage.common.result.Result;
import com.acimage.common.deprecated.annotation.Authentication;
import com.acimage.common.global.enums.AuthenticationType;
import com.acimage.community.global.annotation.TopicId;
import com.acimage.community.global.consts.PageSizeConstants;
import com.acimage.community.model.vo.TopicInfoVo;
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

    @RequestLimit(limitTimes = {5},durations = {10},penaltyTimes = {100},targets = {LimitTarget.ALL})
    @RecordPageView
    @GetMapping("/info/{id}")
    public Result<TopicInfoVo> queryTopicAndFirstCommentPage(@TopicId @Positive @PathVariable("id") Long id) {
        TopicInfoVo topicInfoVo = topicInfoQueryService.getTopicInfoAndFirstCommentPage(id);
        return Result.ok(topicInfoVo);
    }

    @Authentication(type = AuthenticationType.NONE)
    @GetMapping("/recentHot")
    public Result<List<Topic>> queryRecentHotTopics() {
        int rankEnd = 8;
        int pageNo = 1;
        return Result.ok(topicInfoQueryService.pageTopicsInfoRankByPageView(pageNo, rankEnd));
    }

    @Authentication(type = AuthenticationType.NONE)
    @GetMapping("/recommend")
    public Result<List<Topic>> queryRecommendedTopics() {
        int rankEnd = 4;
        int pageNo = 1;
        return Result.ok(topicInfoQueryService.pageTopicsInfoRankByStarCount(pageNo, rankEnd));
    }

    @Authentication(type = AuthenticationType.NONE)
    @GetMapping("/pageRecentTopics/{pageNo}")
    public Result<MyPage<Topic>> pageActiveTopics(@Positive @NotNull @PathVariable int pageNo) {
        return Result.ok(topicInfoQueryService.pageTopicsInfoRankByActivityTime(pageNo, PageSizeConstants.FORUM_TOPICS));
    }

    @GetMapping("/mine/{pageNo}")
    public Result<MyPage<Topic>> queryMyTopics(@Positive @PathVariable("pageNo") int pageNo) {
        return Result.ok(topicInfoQueryService.pageTopicsInfoOrderByCreateTime(UserContext.getUserId(), pageNo, PageSizeConstants.ACTIVITY_TOPICS));
    }
}

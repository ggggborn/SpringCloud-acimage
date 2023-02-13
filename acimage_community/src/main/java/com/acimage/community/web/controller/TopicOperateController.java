package com.acimage.community.web.controller;


import com.acimage.common.deprecated.annotation.Authentication;
import com.acimage.common.global.consts.FileFormatConstants;
import com.acimage.common.global.consts.TimeConstants;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.redis.annotation.RequestLimit;
import com.acimage.common.redis.enums.LimitTarget;
import com.acimage.common.result.Result;
import com.acimage.common.utils.common.FileUtils;
import com.acimage.community.model.request.TopicAddReq;
import com.acimage.community.model.request.TopicModifyHtmlReq;
import com.acimage.community.service.topic.TopicInfoWriteService;
import com.acimage.community.service.topic.TopicWriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;


@RestController
@Slf4j
@RequestMapping("/api/community/topics/operate")
@Validated
@Authentication
public class TopicOperateController {

    @Autowired
    TopicInfoWriteService topicInfoWriteService;
    @Autowired
    TopicWriteService topicWriteService;

    @RequestLimit(limitTimes = {1, 10}, durations = {3, TimeConstants.DAY_SECONDS}, penaltyTimes = {-1, -1}, targets = {LimitTarget.IP, LimitTarget.USER})
    @PostMapping
    public Result<Long> addTopic(@Validated @ModelAttribute TopicAddReq topicAddReq, @RequestParam("coverImage") MultipartFile coverImage) {
        String title = topicAddReq.getTitle().trim();
        topicAddReq.setTitle(title);
        if (title.length() < Topic.TITLE_MIN || title.length() > Topic.TITLE_MAX) {
            return Result.fail(Topic.TAG_VALIDATION_MSG);
        }
        log.info("用户：{} 请求新增话题{}", UserContext.getUsername(), topicAddReq);
        String format = FileUtils.formatOf(coverImage);
        if (!FileFormatConstants.ALLOWED_COVER_IMAGE_FORMAT.contains(format)) {
            return Result.fail("封面图片格式需为" + FileFormatConstants.ALLOWED_IMAGE_FORMAT);
        }
        Integer[] tagIds = topicAddReq.getTagIds();
        if (tagIds.length > Topic.TAG_MAX || tagIds.length < Topic.TAG_MIN) {
            return Result.fail(Topic.TAG_VALIDATION_MSG);
        }

        log.info("用户：{} 话题: 新增话题{}", UserContext.getUsername(), topicAddReq);
        long topicId = topicInfoWriteService.saveTopicInfo(topicAddReq, coverImage);
        return Result.ok(topicId);
    }

    @RequestLimit(limitTimes = {1, 20}, durations = {3, TimeConstants.DAY_SECONDS}, penaltyTimes = {-1, -1}, targets = {LimitTarget.IP, LimitTarget.USER})
    @PutMapping("/title/{id}/{title}")
    public Result<?> modifyTitle(@Positive @PathVariable Long id,
                                 @PathVariable @Size(min = Topic.TITLE_MIN, max = Topic.TITLE_MAX, message = Topic.TITLE_VALIDATION_MSG) String title) {
        topicWriteService.updateTitle(id, title);
        return Result.ok();
    }

    @RequestLimit(limitTimes = {1, 10}, durations = {3, TimeConstants.DAY_SECONDS}, penaltyTimes = {-1, -1}, targets = {LimitTarget.IP, LimitTarget.USER})
    @PutMapping("/html")
    public Result<?> modifyHtml(@Validated @RequestBody TopicModifyHtmlReq topicModifyHtmlReq) {
        topicInfoWriteService.updateHtml(topicModifyHtmlReq);
        return Result.ok();
    }

    @RequestLimit(limitTimes = {1, 20}, durations = {3, TimeConstants.DAY_SECONDS}, penaltyTimes = {-1, -1}, targets = {LimitTarget.IP, LimitTarget.USER})
    @DeleteMapping("/{id}")
    public Result<?> deleteTopic(@Positive @PathVariable("id") Long id) {
        log.info("删除了话题：{}", id);
        topicInfoWriteService.removeTopicInfo(id);
        return Result.ok();
    }


}

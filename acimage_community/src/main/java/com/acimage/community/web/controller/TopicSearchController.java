package com.acimage.community.web.controller;


import com.acimage.common.global.annotation.Authentication;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.global.enums.AuthenticationType;
import com.acimage.common.result.Result;
import com.acimage.community.global.annotation.TopicId;
import com.acimage.community.global.consts.PageSizeConsts;
import com.acimage.community.model.vo.TopicInfoVo;
import com.acimage.community.service.topic.TopicInfoQueryService;
import com.acimage.community.service.topic.annotation.RecordPageView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@RestController
@Slf4j
@RequestMapping("/api/community/topics/search")
@Validated
@Authentication
public class TopicSearchController {

}

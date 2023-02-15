package com.acimage.community.web.controller;


import com.acimage.common.model.domain.community.CmtyUser;
import com.acimage.common.model.page.MyPage;
import com.acimage.common.redis.annotation.RequestLimit;
import com.acimage.common.redis.enums.LimitTarget;
import com.acimage.common.result.Result;
import com.acimage.common.utils.LambdaUtils;
import com.acimage.community.service.cmtyuser.CmtyUserRankService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/community/users/rank")
@Validated
public class UserRankController {

    @Autowired
    CmtyUserRankService cmtyUserRankService;

    @RequestLimit(limitTimes = {15}, durations = {5}, penaltyTimes = {-1}, targets = {LimitTarget.IP})
    @GetMapping("/byColumn")
    public Result<MyPage<CmtyUser>> rankByTopicCount(@RequestParam @Range(min=1,max=10) Integer pageNo,
                                                       @RequestParam String column) {
        List<String> allowedColumns= LambdaUtils.columnsFrom(CmtyUser::getTopicCount,CmtyUser::getStarCount);
        if(!allowedColumns.contains(column)){
            return Result.fail("非法字段");
        }
        int pageSize=10;
        return Result.ok(cmtyUserRankService.pageUserRankBy(column,pageNo, pageSize));
    }


}

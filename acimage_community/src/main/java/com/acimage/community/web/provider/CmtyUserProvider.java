package com.acimage.community.web.provider;


import com.acimage.common.deprecated.annotation.Authentication;
import com.acimage.common.model.domain.community.CmtyUser;
import com.acimage.common.result.Result;
import com.acimage.community.service.cmtyuser.CmtyUserQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/community/cmtyUsers")
@Validated
public class CmtyUserProvider {
    @Autowired
    CmtyUserQueryService cmtyUserQueryService;

    @GetMapping("/userId/{userId}")
    public Result<CmtyUser> queryCmtyUser(@PathVariable Long userId) {
        return Result.ok(cmtyUserQueryService.getCmtyUser(userId));
    }
}

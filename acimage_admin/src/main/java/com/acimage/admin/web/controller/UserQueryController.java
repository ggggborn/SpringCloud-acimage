package com.acimage.admin.web.controller;


import com.acimage.admin.model.request.UserQueryReq;
import com.acimage.admin.service.user.UserQueryService;
import com.acimage.common.model.domain.user.User;
import com.acimage.common.model.page.MyPage;
import com.acimage.common.result.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/admin/users/query")
@Validated
public class UserQueryController {
    @Autowired
    UserQueryService userQueryService;

    @GetMapping("/search")
    public Result<MyPage<User>> queryUsersBy(@Validated @ModelAttribute UserQueryReq userQueryReq){
        return Result.ok(userQueryService.listBy(userQueryReq));
    }
}

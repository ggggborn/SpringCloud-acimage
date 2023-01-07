package com.acimage.user.web.provider;



import com.acimage.common.global.annotation.Authentication;
import com.acimage.common.global.enums.AuthenticationType;
import com.acimage.common.model.domain.User;
import com.acimage.common.result.Result;
import com.acimage.user.service.user.UserQueryService;
import com.acimage.user.service.user.UserWriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequestMapping("/api/user/users")
@Validated
@Authentication
public class UserProvider {
    @Autowired
    UserQueryService userQueryService;
    @Autowired
    UserWriteService userWriteService;

    @Authentication(type = AuthenticationType.NONE)
    @GetMapping("/id/{id}")
    public Result<User> queryUser(@PathVariable @Positive Long id) {
        User user = userQueryService.getUser(id);
        return Result.ok(user);
    }


    @PutMapping("/photoUrl")
    Result<String> modifyPhotoUrl(@RequestBody @NotNull String photoUrl){
        String newToken=userWriteService.updatePhotoUrl(photoUrl);
        return Result.ok(newToken);
    }
}

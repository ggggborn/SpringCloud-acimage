package com.acimage.feign.client;

import com.acimage.common.model.domain.user.User;
import com.acimage.common.result.Result;
import com.acimage.feign.fallback.UserClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(value="user-service/user/users",fallbackFactory = UserClientFallbackFactory.class)
public interface UserClient {
    @GetMapping("/id/{id}")
    Result<User> queryUser(@PathVariable Long id);

    @PutMapping("/photoUrl")
    Result<String> modifyPhotoUrl(@RequestBody String photoUrl);
}

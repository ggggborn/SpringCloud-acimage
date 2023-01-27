package com.acimage.feign.fallback;

import com.acimage.common.global.context.UserContext;
import com.acimage.common.model.domain.user.User;
import com.acimage.common.result.Result;
import com.acimage.feign.client.UserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable cause) {
        return new UserClient() {
            @Override
            public Result<User> queryUser(Long id) {
                cause.printStackTrace();
                log.error("查询失败，用户id:{}", id);
                return Result.ok(new User());
            }

            @Override
            public Result<String> modifyPhotoUrl(String photoUrl) {
                cause.printStackTrace();
                log.error("修改photoUrl失败，用户id:{}", UserContext.getUsername());
                return Result.fail("头像修改失败");
            }
        };
    }
}

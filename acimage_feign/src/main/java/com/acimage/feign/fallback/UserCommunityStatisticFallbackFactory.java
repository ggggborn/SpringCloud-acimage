package com.acimage.feign.fallback;

import com.acimage.common.model.domain.community.UserCommunityStatistic;
import com.acimage.common.result.Result;
import com.acimage.feign.client.UserCommunityStatisticClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserCommunityStatisticFallbackFactory implements FallbackFactory<UserCommunityStatisticClient> {
    @Override
    public UserCommunityStatisticClient create(Throwable cause) {
        return new UserCommunityStatisticClient() {
            @Override
            public Result<UserCommunityStatistic> queryUserCommunityStatistic(Long userId) {
                cause.printStackTrace();
                log.error("查询UserCommunityStatistic失败，userId:{}", userId);
                return Result.ok(new UserCommunityStatistic());
            }
        };
    }
}

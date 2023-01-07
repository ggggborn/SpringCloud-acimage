package com.acimage.feign.client;


import com.acimage.common.model.domain.UserCommunityStatistic;
import com.acimage.common.result.Result;
import com.acimage.feign.fallback.UserCommunityStatisticFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value="community-service/api/community/userStatistics",fallbackFactory = UserCommunityStatisticFallbackFactory.class)
public interface UserCommunityStatisticClient {

    @GetMapping("/userId/{userId}")
    Result<UserCommunityStatistic> queryUserCommunityStatistic(@PathVariable Long userId);
}

package com.acimage.feign.client;


import com.acimage.common.model.domain.community.CmtyUser;
import com.acimage.common.result.Result;
import com.acimage.feign.fallback.CmtyUserClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value="community-service/community/cmtyUsers",fallbackFactory = CmtyUserClientFallbackFactory.class)
public interface CmtyUserClient {

    @GetMapping("/userId/{userId}")
    Result<CmtyUser> queryCmtyUser(@PathVariable Long userId);
}

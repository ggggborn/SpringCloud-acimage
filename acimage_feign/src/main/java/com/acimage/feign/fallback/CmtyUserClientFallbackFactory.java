package com.acimage.feign.fallback;

import com.acimage.common.model.domain.community.CmtyUser;
import com.acimage.common.result.Result;
import com.acimage.common.utils.ExceptionUtils;
import com.acimage.feign.client.CmtyUserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CmtyUserClientFallbackFactory implements FallbackFactory<CmtyUserClient> {
    @Override
    public CmtyUserClient create(Throwable cause) {
        return new CmtyUserClient() {
            @Override
            public Result<CmtyUser> queryCmtyUser(Long userId) {
                ExceptionUtils.printIfDev(cause);
                log.error("查询UserCommunityStatistic失败，userId:{}", userId);
                return Result.ok(new CmtyUser());
            }
        };
    }
}

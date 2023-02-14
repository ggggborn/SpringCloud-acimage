package com.acimage.feign.fallback;


import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.result.Result;
import com.acimage.common.utils.ExceptionUtils;
import com.acimage.feign.client.CommentClient;
import com.acimage.feign.client.TopicClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CommentClientFallbackFactory implements FallbackFactory<CommentClient> {
    @Override
    public CommentClient create(Throwable cause) {
        return new CommentClient() {
            @Override
            public Result<?> delete(Long id) {
                log.error("删除评论失败 id:{} error:{}", id, cause.getMessage());
                return Result.fail("删除评论失败");
            }
        };
    }
}

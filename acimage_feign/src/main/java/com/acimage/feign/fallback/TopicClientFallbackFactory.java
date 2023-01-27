package com.acimage.feign.fallback;


import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.result.Result;
import com.acimage.feign.client.TopicClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TopicClientFallbackFactory implements FallbackFactory<TopicClient> {
    @Override
    public TopicClient create(Throwable cause) {
        return new TopicClient() {
            @Override
            public Result<List<Topic>> queryTopics(List<Long> topicIds) {
                cause.printStackTrace();
                log.error("查询多个话题失败 topicIds:{}", topicIds);
                return Result.ok(new ArrayList<>());
            }
        };
    }
}

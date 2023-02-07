package com.acimage.feign.client;


import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.result.Result;
import com.acimage.feign.fallback.TopicClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value="community-service/community/topics",fallbackFactory = TopicClientFallbackFactory.class)
public interface TopicClient {

    @GetMapping("/ids")
    Result<List<Topic>> queryTopics(@RequestParam("topicIds") List<Long> topicIds);

    @DeleteMapping("/{topicId}")
    public Result<?> delete(@PathVariable("topicId") Long topicId) ;
}

package com.acimage.feign.client;


import com.acimage.common.result.Result;
import com.acimage.feign.fallback.CommentClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

@FeignClient(value="community-service/community/comments",fallbackFactory = CommentClientFallbackFactory.class)
public interface CommentClient {

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable("id") Long id) ;
}

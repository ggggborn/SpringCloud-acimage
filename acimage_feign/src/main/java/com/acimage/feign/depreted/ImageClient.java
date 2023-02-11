package com.acimage.feign.depreted;



import com.acimage.common.model.domain.image.Image;
import com.acimage.feign.fallback.ImageClientFallbackFactory;
import com.acimage.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value="image-service/api/image/images",fallbackFactory = ImageClientFallbackFactory.class)
public interface ImageClient {
    @GetMapping("/imageIds")
    Result queryImagesWithTopic(@RequestParam("imageIds") List<Long> imageIds);

    @GetMapping("/topicId/{topicId}")
    Result<List<Image>> queryTopicImages(@PathVariable("topicId") Long topicId) ;


    @GetMapping("/preparedTopicImages")
    Result<String> updateTopicIdAndReturnFirstImageUrl(@RequestParam("serviceToken") String serviceToken,
                                                              @RequestParam("topicId") Long topicId);
}

package com.acimage.feign.fallback;

import com.acimage.common.model.domain.image.Image;
import com.acimage.common.utils.ExceptionUtils;
import com.acimage.common.utils.SpringContextUtils;
import com.acimage.feign.depreted.ImageClient;
import com.acimage.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
public class ImageClientFallbackFactory implements FallbackFactory<ImageClient> {

    @Override
    public ImageClient create(Throwable cause) {
        return new ImageClient() {
            @Override
            public Result queryImagesWithTopic(List<Long> imageIds) {
                if(SpringContextUtils.isDev()){
                    ExceptionUtils.printIfDev(cause);
                }

                log.error("feign 查询失败，imageIds:{}",imageIds);
                return Result.fail("服务繁忙，查询失败");
            }

            @Override
            public Result<List<Image>> queryTopicImages(Long topicId) {
                ExceptionUtils.printIfDev(cause);
                log.error("feign 查询话题图片失败，topicId:{}",topicId);
                return Result.ok(new ArrayList<>());
            }

            @Override
            public Result<String> updateTopicIdAndReturnFirstImageUrl(String serviceToken, Long topicId) {
                ExceptionUtils.printIfDev(cause);
                log.error("feign topicId提交到image-service失败，topicId:{}",topicId);
                return Result.fail("服务繁忙，查询失败");
            }
        };
    }
}

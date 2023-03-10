package com.acimage.common.model.mq.dto;

import com.acimage.common.global.enums.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyncImagesUpdateDto {
    private Long topicId;
    /**
     * 话题内新增的图片链接
     */
    private List<String> addImageUrls;
    /**
     * 话题内移除的图片链接
     */
    private List<String> removeImageUrls;

    private ServiceType serviceType;
}

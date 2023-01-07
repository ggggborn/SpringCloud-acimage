package com.acimage.community.model.request;

import com.acimage.common.model.domain.Topic;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;


@Data
@NoArgsConstructor
public class TopicModifyContentReq {
    @Positive
    Long id;
    @Size(min = Topic.CONTENT_MIN, max = Topic.CONTENT_MAX, message = Topic.CONTENT_VALIDATION_MSG)
    private String content;
}

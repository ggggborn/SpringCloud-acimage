package com.acimage.community.model.request;

import com.acimage.common.model.domain.community.Topic;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class TopicAddReqBak {

    @Size(min = Topic.CONTENT_MIN, max = Topic.CONTENT_MAX, message = Topic.CONTENT_VALIDATION_MSG)
    private String content;
    @Size(min = Topic.TITLE_MIN, max = Topic.TITLE_MAX, message = Topic.TITLE_VALIDATION_MSG)
    private String title;
}

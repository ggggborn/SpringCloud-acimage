package com.acimage.community.model.request;


import com.acimage.common.model.domain.Topic;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class TopicAddReqBak2 {

    @NotNull
    String serviceToken;

    @Size(min = Topic.CONTENT_MIN, max = Topic.CONTENT_MAX, message = Topic.CONTENT_VALIDATION_MSG)
    private String content;

    @Size(min = Topic.TITLE_MIN, max = Topic.TITLE_MAX, message = Topic.TITLE_VALIDATION_MSG)
    private String title;



}

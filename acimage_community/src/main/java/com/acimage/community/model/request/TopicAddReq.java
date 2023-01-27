package com.acimage.community.model.request;

import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.domain.community.TopicHtml;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class TopicAddReq {
    @Size(min = Topic.TITLE_MIN, max = Topic.TITLE_MAX, message = Topic.TITLE_VALIDATION_MSG)
    private String title;
    @Size(min= TopicHtml.HTML_MIN,max = TopicHtml.HTML_MAX,message = TopicHtml.HTML_VALIDATION_MSG)
    private String html;
}

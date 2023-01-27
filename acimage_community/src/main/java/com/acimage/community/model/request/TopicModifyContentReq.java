package com.acimage.community.model.request;

import com.acimage.common.model.domain.community.TopicHtml;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;


@Data
@NoArgsConstructor
public class TopicModifyContentReq {
    @Positive
    Long id;
    @Size(min = TopicHtml.HTML_MIN, max = TopicHtml.HTML_MAX, message = TopicHtml.HTML_VALIDATION_MSG)
    private String html;
}

package com.acimage.community.model.request;

import com.acimage.common.model.domain.Topic;
import com.acimage.common.model.domain.Image;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class TopicModifyReq {

    @Positive
    @NotNull(message = "id不能为空")
    Long id;

    @Size(min = Topic.CONTENT_MIN, max = Topic.CONTENT_MAX, message = Topic.CONTENT_VALIDATION_MSG)
    private String content;

    @Size(min = Topic.TITLE_MIN, max = Topic.TITLE_MAX, message = Topic.TITLE_VALIDATION_MSG)
    private String title;
    List<@Size(min = Image.DESCRIPTION_MIN, max = Image.DESCRIPTION_MAX,
            message = Image.DESCRIPTION_VALIDATION_MSG) String> descriptions;
}

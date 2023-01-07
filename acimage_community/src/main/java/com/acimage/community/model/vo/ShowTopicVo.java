package com.acimage.community.model.vo;

import com.acimage.common.model.domain.Topic;
import lombok.Data;

@Deprecated
@Data
public class ShowTopicVo {
    Topic topic;
    Boolean isStar=false;
}

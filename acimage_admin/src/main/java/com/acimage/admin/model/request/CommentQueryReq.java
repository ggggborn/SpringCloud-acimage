package com.acimage.admin.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class CommentQueryReq {
    Long topicId;
    String keyword;
    @Range(min=1)
    Integer pageNo;
    @Range(min=5,max=20)
    Integer pageSize;
}

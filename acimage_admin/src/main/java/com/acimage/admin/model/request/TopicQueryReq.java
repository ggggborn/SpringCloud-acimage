package com.acimage.admin.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class TopicQueryReq {
    @NotBlank
    String column;
    @Positive
    @NotNull
    Integer pageNo;
    @Range(min=5,max=20)
    Integer pageSize;
}

package com.acimage.admin.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class TopicQueryReq {
    @NotBlank
    String column;
    @Positive
    @NotNull
    Integer pageNo;
    @Min(2)
    @Max(10)
    Integer pageSize;
}

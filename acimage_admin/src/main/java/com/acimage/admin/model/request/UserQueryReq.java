package com.acimage.admin.model.request;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class UserQueryReq {

    String keyword;
    @NotNull
    @Positive
    Integer pageNo;
    @Min(2)
    @Max(20)
    Integer pageSize;
}

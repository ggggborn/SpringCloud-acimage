package com.acimage.admin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiQueryReq {
    String keyword;
    @Min(1)
    Integer pageNo;
    @Min(2)
    @Max(20)
    Integer pageSize;
}

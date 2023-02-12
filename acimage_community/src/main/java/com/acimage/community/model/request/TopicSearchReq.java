package com.acimage.community.model.request;


import com.acimage.community.global.enums.SortMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicSearchReq {


    private Integer categoryId;
    private Integer tagId;
    @Positive
    @NotNull
    @Max(30)
    private Integer pageNo;
    @Size(max = 15, message = "搜索字数不超过15")
    private String search;
    private SortMode sortMode;
}



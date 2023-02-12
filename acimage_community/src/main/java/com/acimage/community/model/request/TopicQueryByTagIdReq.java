package com.acimage.community.model.request;

import com.acimage.community.global.enums.SortMode;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class TopicQueryByTagIdReq {
    @Positive
    @NotNull
    private Integer tagId;
    @Min(1)
    @Max(50)
    private Integer pageNo;
    @Min(1)
    @Max(20)
    private Integer pageSize;
    private SortMode sortMode;
}

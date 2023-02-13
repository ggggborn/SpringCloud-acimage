package com.acimage.community.model.request;

import com.acimage.community.global.enums.SortMode;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class TopicQueryBySortReq {
    @Range(min=1,max=100,message = "页码在1到100之间")
    private Integer pageNo;
    @Range(min=4,max=20,message = "页大小在4到20之间")
    private Integer pageSize;
    @NotNull
    private SortMode sortMode;
}

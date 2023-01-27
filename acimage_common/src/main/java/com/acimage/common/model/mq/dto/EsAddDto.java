package com.acimage.common.model.mq.dto;

import com.acimage.common.model.dto.ObjectWithClass;
import com.acimage.common.utils.common.JacksonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;


@Data
@NoArgsConstructor
public class EsAddDto extends ObjectWithClass {
    public EsAddDto(Object obj){
        this.from(obj);
    }

}

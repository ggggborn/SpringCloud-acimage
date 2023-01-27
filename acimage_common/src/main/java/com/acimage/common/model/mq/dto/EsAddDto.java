package com.acimage.common.model.mq.dto;

import lombok.*;


@Data
@NoArgsConstructor
public class EsAddDto extends ObjectWithClass {
    public EsAddDto(Object obj){
        this.from(obj);
    }

}

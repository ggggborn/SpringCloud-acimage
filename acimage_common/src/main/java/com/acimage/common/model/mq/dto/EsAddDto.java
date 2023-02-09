package com.acimage.common.model.mq.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
public class EsAddDto extends ObjectWithClass {
    public EsAddDto(Object obj){
        this.with(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

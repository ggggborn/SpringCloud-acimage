package com.acimage.common.model.mq.dto;

import com.acimage.common.model.dto.ObjectWithClass;
import lombok.*;


@Data
@NoArgsConstructor
public class EsAddDto extends ObjectWithClass{

    public EsAddDto(Object obj){
        this.setObject(obj);
    }

}

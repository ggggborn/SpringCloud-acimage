package com.acimage.common.model.mq.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EsUpdateByIdDto extends ObjectWithClass {
    List<String> columns;

    @Override
    public String toString() {
        return columns+super.toString();
    }
    //    List<SFunction<Object, Object>> columns;

}

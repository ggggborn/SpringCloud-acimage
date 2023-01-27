package com.acimage.common.model.mq.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
public class EsUpdateDto extends ObjectWithClass {
    List<String> columns;

//    List<SFunction<Object, Object>> columns;

}

package com.acimage.common.model.mq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EsDeleteDto {
    String id;
    Class<?> type;
}

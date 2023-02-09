package com.acimage.common.model.mq.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EsUpdateByTermDto extends ObjectWithClass{
    String termColumn;
    List<String> columns;

    @Override
    public String toString() {
        return "EsUpdateByTermDto{" +
                "termColumn='" + termColumn + '\'' +
                ", columns=" + columns +
                '}'+super.toString();
    }
}

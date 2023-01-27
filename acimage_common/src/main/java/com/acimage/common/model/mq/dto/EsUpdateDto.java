package com.acimage.common.model.mq.dto;

import com.acimage.common.model.dto.ObjectWithClass;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
public class EsUpdateDto extends ObjectWithClass {

    List<SFunction<Object, Object>> columns;

}

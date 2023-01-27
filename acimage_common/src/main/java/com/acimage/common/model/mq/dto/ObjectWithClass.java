package com.acimage.common.model.mq.dto;

import com.acimage.common.utils.common.JacksonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于序列化时候传输
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObjectWithClass {
    private String json;
    private Class<?> type;

    public void from(Object obj){
        this.setJson(JacksonUtils.writeValueAsString(obj));
        this.setType(obj.getClass());
    }

    /**
     * 千万别写成getObject,否则会导致序列化出错
     */
    public Object object(){
        return JacksonUtils.convert(json,type);
    }
}

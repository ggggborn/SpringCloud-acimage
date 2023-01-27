package com.acimage.common.model.dto;

import com.acimage.common.utils.common.JacksonUtils;
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
    String json;
    Class<?> type;

    public void setObject(Object obj){
        this.setJson(JacksonUtils.writeValueAsString(obj));
        this.setType(obj.getClass());

    }

    public Object getObject(){
        return JacksonUtils.convert(json,type);
    }
}

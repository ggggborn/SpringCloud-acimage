package com.acimage.common.utils.common;


import com.acimage.common.utils.ExceptionUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.List;

@Slf4j
public class JacksonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        //设置忽略空字段
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String writeValueAsString(Object object) {
        String json = null;
        try {
            json = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            ExceptionUtils.printIfDev(e);
            log.error("对象序列化成json错误 对象[{}] error:{}", object,e.getMessage());
            throw new RuntimeException(e);
        }
        return json;
    }

    public static <T> List<T> getList(@Nullable String json, Class<T> ofType) {
        if (json == null) {
            return null;
        }
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, ofType);
        try {
            return mapper.readValue(json, javaType);
        } catch (JsonProcessingException e) {
            log.error("数据反序列化异常 json：{} type:{}", json,javaType);
            throw new RuntimeException(e);
        }
    }

    public static <T> T convert(@Nullable String json, Class<T> targetType) {
        if (json == null) {
            return null;
        }

        try {
            return mapper.readValue(json, targetType);
        } catch (JsonProcessingException e) {
            log.error("数据反序列化异常 json：{} type:{}", json,targetType);
            throw new RuntimeException(e);
        }
    }
}

package com.acimage.common.utils.common;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.lang.reflect.Method;
import java.util.*;


public class BeanUtils {

    public static Map<String, String> beanToFieldJsonMap(Object javaBean) {

        Map<String, Object> map = BeanUtil.beanToMap(javaBean, false, true);
        Map<String, String> fieldJsonMap = new HashMap<>();
        Set<String> keys = map.keySet();
        for (String key : keys) {
            Object obj = map.get(key);
            String json = JacksonUtils.writeValueAsString(obj);
            fieldJsonMap.put(key, json);
        }
        return fieldJsonMap;
    }

    public static <T> T fieldJsonMapToBean(Map<String, String> fieldJsonMap, Class<T> clz) {
        T instance=ReflectUtil.newInstance(clz);
        List<String> fieldName = ReflectUtils.getFieldNames(clz);
        Set<String> keys = fieldJsonMap.keySet();

        for (String key : keys) {
            if (fieldName.contains(key)) {
                String json = fieldJsonMap.get(key);
                //json转对象
                Object obj = JacksonUtils.convert(json, ReflectUtil.getField(clz, key).getType());
                //获取方法
                String setMethodName = StringUtils.concatCapitalize("set", key);
                Method setMethod = ReflectUtil.getMethodByName(clz, setMethodName);
                //调用方法，将属性set进去
                ReflectUtil.invoke(instance, setMethod, obj);
            }
        }

        return instance;
    }

    public static <T> T copyPropertiesTo(Object source,Class<T> targetType){
        T target=ReflectUtil.newInstance(targetType);
        BeanUtil.copyProperties(source,target,false);
        return target;
    }

}

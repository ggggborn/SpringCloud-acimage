package com.acimage.common.utils.common;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ReflectUtils {

    public static Class<?>[] genericsOfReturnType(Method method) throws ClassNotFoundException {
        Class<?>[] generics = new Class<?>[]{};

        Type genericReturnType = method.getGenericReturnType();
        if (genericReturnType instanceof ParameterizedType ) {
            ParameterizedType parameterizedType=(ParameterizedType) genericReturnType;
            Type[] types = parameterizedType.getActualTypeArguments();
            generics = new Class<?>[types.length];
            for (int i = 0; i < types.length; i++) {

                if (types[i] instanceof WildcardType) {
                    generics[i] = Object.class;
                } else {
                    generics[i] = Class.forName(types[i].getTypeName());
                }

            }
        }
        return generics;
    }

    public static List<String> getFieldNames(Class<?> clz){
        Field[] fields=clz.getDeclaredFields();
        List<String> fileNameList=new ArrayList<>();
        for(Field field:fields){
            fileNameList.add(field.getName());
        }
        return fileNameList;
    }

    public static Object getAnnotatedFiled(Object obj,Class<? extends Annotation> annotation){
        Field field = ReflectUtils.firstAnnotatedField(obj.getClass(), annotation);
        if (field == null) {
            return null;
        }

        String fieldName = field.getName();
        return BeanUtil.getFieldValue(obj,fieldName);
//
//        String getMethodName = StringUtils.concatCapitalize("get", fieldName);
//        Method method;
//        try {
//            method = obj.getClass().getMethod(getMethodName);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//            log.error("反射获取class对象[{}]方法[{}]失败", annotation, getMethodName);
//            throw new RuntimeException(e);
//        }
//
//        try {
//            return method.invoke(obj);
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            throw new RuntimeException(e);
//        }
    }

    public static <T extends Annotation> Field firstAnnotatedField(Class<?> objectClass, Class<T> annotation) {
        Field[] fields = objectClass.getDeclaredFields();
        for (Field field : fields) {
            boolean isAnnotated = field.isAnnotationPresent(annotation);
            if (isAnnotated) {
                return field;
            }
        }
        return null;
    }





}

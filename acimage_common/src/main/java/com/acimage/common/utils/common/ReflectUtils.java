package com.acimage.common.utils.common;

import cn.hutool.core.util.ReflectUtil;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class ReflectUtils {

    public static Class<?>[] genericsOfReturnType(Method method) throws ClassNotFoundException {
        Class<?>[] generics = new Class<?>[]{};

        Type genericReturnType = method.getGenericReturnType();
        if (genericReturnType instanceof ParameterizedType parameterizedType) {
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





}

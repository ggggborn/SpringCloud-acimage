package com.acimage.common.utils;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class LambdaUtils {
    public static final String TOKEN_GET="get";
    public static final String TOKEN_IS="get";
    public static final String WRITE_REPLACE="writeReplace";

    public static <T> String getUnderlineColumnName(SFunction<T, ?> getOrIs) {
        return StringUtils.camelToUnderline(columnNameOf(getOrIs));

    }

    public static <T> String columnNameOf(SFunction<T, ?> getOrIs) {

        // 从function取出序列化方法
        Method writeReplaceMethod = ReflectUtil.getMethodByName(getOrIs.getClass(), WRITE_REPLACE);
        // 从序列化方法取出序列化的lambda信息
        boolean isAccessible = writeReplaceMethod.isAccessible();
        writeReplaceMethod.setAccessible(true);
        SerializedLambda serializedLambda;
        try {
            serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(getOrIs);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        writeReplaceMethod.setAccessible(isAccessible);

        // 从lambda信息取出method等
        String methodName = serializedLambda.getImplMethodName();
        String capitalizeFieldName;
        if (methodName.startsWith(TOKEN_IS)) {
            capitalizeFieldName = methodName.substring(TOKEN_IS.length());
        } else if (methodName.startsWith(TOKEN_GET)) {
            capitalizeFieldName = methodName.substring(TOKEN_GET.length());
        } else {
            throw new IllegalArgumentException(methodName + "前缀非get或is");
        }
        return StringUtils.firstToLowerCase(capitalizeFieldName);
    }

    @SafeVarargs
    public static <T> List<String> columnsFrom(SFunction<T, ?>... getOrIsFunctions) {
        List<String> columns=new ArrayList<>();
        for(SFunction<T,?> function:getOrIsFunctions){
            columns.add(columnNameOf(function));
        }
        return columns;
    }
}

package com.acimage.common.utils;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.extern.slf4j.Slf4j;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LambdaUtils {
    public static final String TOKEN_GET="get";
    public static final String TOKEN_IS="is";
    public static final String WRITE_REPLACE="writeReplace";

    public static <T> String underlineColumnNameOf(SFunction<T, ?> getOrIs) {
        return StringUtils.camelToUnderline(columnOf(getOrIs));

    }

    public static <T> String columnOf(SFunction<T, ?> getOrIs) {

        // 从function取出序列化方法
        Method writeReplaceMethod = ReflectUtil.getMethodByName(getOrIs.getClass(), WRITE_REPLACE);
        // 从序列化方法取出序列化的lambda信息
//        boolean isAccessible = writeReplaceMethod.isAccessible();
        /**
         * 如果要通过isAccessible设置回去，一定要考虑并发问题
         */
        writeReplaceMethod.setAccessible(true);
        SerializedLambda serializedLambda;
        try {
            serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(getOrIs);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        //如果将访问属性设置回去，这里可能会出错
        //writeReplaceMethod.setAccessible(isAccessible);

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
            columns.add(columnOf(function));
        }
        return columns;
    }
}

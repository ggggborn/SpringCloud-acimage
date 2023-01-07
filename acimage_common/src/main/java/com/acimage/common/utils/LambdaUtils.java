package com.acimage.common.utils;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LambdaUtils {

    public static <T> String getUnderlineColumnName(SFunction<T, ?> getOrIs) {

        return StringUtils.camelToUnderline(getCamelColumnName(getOrIs));
        //field = Class.forName(serializedLambda.getImplClass().replace("/", ".")).getDeclaredField(capitalizeFieldName);

    }

    public static <T> String getCamelColumnName(SFunction<T, ?> getFunctionOrIsFunction) {

        // 从function取出序列化方法
        Method writeReplaceMethod = ReflectUtil.getMethodByName(getFunctionOrIsFunction.getClass(), "writeReplace");
        // 从序列化方法取出序列化的lambda信息
        boolean isAccessible = writeReplaceMethod.trySetAccessible();
        writeReplaceMethod.setAccessible(true);
        SerializedLambda serializedLambda;
        try {
            serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(getFunctionOrIsFunction);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        writeReplaceMethod.setAccessible(isAccessible);

        // 从lambda信息取出method等
        String methodName = serializedLambda.getImplMethodName();
        String capitalizeFieldName;
        if (methodName.startsWith("is")) {
            capitalizeFieldName = methodName.substring("is".length());
        } else if (methodName.startsWith("get")) {
            capitalizeFieldName = methodName.substring("get".length());
        } else {
            throw new RuntimeException(methodName + "前缀非get或is");
        }

        return StringUtils.firstToLowerCase(capitalizeFieldName);

        //field = Class.forName(serializedLambda.getImplClass().replace("/", ".")).getDeclaredField(capitalizeFieldName);
    }
}

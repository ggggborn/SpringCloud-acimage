package com.acimage.common.utils.common;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AopUtils {
    public static Method methodOf(@NotNull JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method;
        try {
            method = joinPoint.getTarget()
                    .getClass()
                    .getMethod(signature.getMethod().getName(),
                            signature.getMethod().getParameterTypes());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return method;
    }

    public static boolean hasParameters(@NotNull JoinPoint joinPoint) {
        Parameter[] parameters = methodOf(joinPoint).getParameters();
        return parameters != null && parameters.length != 0;
    }

    public static <T extends Annotation> T annotationFrom(JoinPoint joinPoint, Class<T> annotation) {
        Method method = methodOf(joinPoint);
        return method.getAnnotation(annotation);
    }

    public static <T extends Annotation> List<T> paramAnnotationsFrom(JoinPoint joinPoint, Class<T> annotation) {
        Parameter[] parameters = methodOf(joinPoint).getParameters();
        List<T> annotationList=new ArrayList<>();
        for(int i=0;i<parameters.length;i++){
            if(parameters[i].isAnnotationPresent(annotation)){
                annotationList.add(parameters[i].getAnnotation(annotation));
            }
        }
        return annotationList;
    }

    /**
     * 返回首个被annotation注解的实参或实参成员变量
     */
//    public static <T extends Annotation, V> V annotatedArgOrArgFieldOf(@NotNull JoinPoint joinPoint, Class<T> annotation, Class<V> targetType) {
//
//        Parameter[] parameters = methodOf(joinPoint).getParameters();
//        Object[] args = joinPoint.getArgs();
//
//        Integer firstIndex = indexOfFirstAnnotatedParameter(parameters, annotation);
//        if (firstIndex != null) {
//            return Convert.convert(targetType, args[firstIndex]);
//        }
//
//        for (Parameter parameter : parameters) {
//            Field field = firstAnnotatedField(parameter.getType(), annotation);
//            if (field == null) {
//                continue;
//            }
//
//            String fieldName = field.getName();
//
//            String getMethodName = StringUtils.concatCapitalize("get", fieldName);
//            Method method;
//            try {
//                method = parameter.getType().getMethod(getMethodName);
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//                log.error("反射获取class对象[{}]方法[{}]失败", annotation, getMethodName);
//                throw new RuntimeException(e);
//            }
//
//            try {
//                Object result = method.invoke();
//                return Convert.convert(targetType, result);
//            } catch (IllegalAccessException | InvocationTargetException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        return null;
//    }

    public static <T extends Annotation, V> V annotatedArgOrArgFieldOf(@NotNull JoinPoint joinPoint, Class<T> annotation, Class<V> targetType) {

        Parameter[] parameters = methodOf(joinPoint).getParameters();
        Object[] args = joinPoint.getArgs();

        Integer firstIndex = indexOfFirstAnnotatedParameter(parameters, annotation);
        if (firstIndex != null) {
            return Convert.convert(targetType, args[firstIndex]);
        }

        for(int i=0;i<parameters.length;i++){

            Parameter parameter=parameters[i];
            Field field = ReflectUtils.firstAnnotatedField(parameter.getType(), annotation);
            if (field == null) {
                continue;
            }

            String fieldName = field.getName();
            Object result= BeanUtil.getFieldValue(args[i],fieldName);
            return Convert.convert(targetType, result);
//            String getMethodName = StringUtils.concatCapitalize("get", fieldName);
//            Method method;
//            try {
//                method = parameter.getType().getMethod(getMethodName);
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//                log.error("反射获取class对象[{}]方法[{}]失败", annotation, getMethodName);
//                throw new RuntimeException(e);
//            }
//
//            try {
//                Object result = method.invoke(args[i]);
//                return Convert.convert(targetType, result);
//            } catch (IllegalAccessException | InvocationTargetException e) {
//                throw new RuntimeException(e);
//            }
        }

        return null;
    }

    /**
     * 返回首个被annotation注解的实参或实参成员变量
     */
    public static <T extends Annotation, V> List<V> annotatedArgsFrom(@NotNull JoinPoint joinPoint, Class<T> annotation, Class<V> targetType) {

        Parameter[] parameters = methodOf(joinPoint).getParameters();
        Object[] args = joinPoint.getArgs();
        List<V> annotatedObjectList=new ArrayList<>();

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(annotation)) {
                annotatedObjectList.add(Convert.convert(targetType, args[i]));
            }
        }

        return annotatedObjectList;
    }

    private static <T extends Annotation> Integer indexOfFirstAnnotatedParameter(Parameter[] parameters, Class<T> annotation) {
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(annotation)) {
                return i;
            }
        }
        return null;
    }




}

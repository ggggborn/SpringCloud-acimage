package com.acimage.common.redis.utils;

import com.acimage.common.redis.annotation.KeyParam;
import com.acimage.common.utils.common.AopUtils;
import com.acimage.common.utils.RedisUtils;
import com.acimage.common.utils.common.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisAnnotationUtils {

    @Autowired
    RedisUtils redisUtils;
    /**
     * 查询redis，有则返回，无则调用原方法被设置到redis并返回
     */
    private  Object queryOrProceed(ProceedingJoinPoint joinPoint, String redisKey, Class<?> resultType, long expire, TimeUnit unit) throws Throwable {
        //查redis
        Object objectResult = redisUtils.getObjectFromString(redisKey, resultType);
        if (objectResult != null) {
            return objectResult;
        }

        Object result = joinPoint.proceed();

        //设置到redis
        redisUtils.setObjectJson(redisKey, result, expire, unit);
        return result;
    }

    /**
     * 查询redis，有则返回，无则调用原方法被设置到redis并返回，针对返回值是list的类型
     */
    private Object queryOrProceedForList(ProceedingJoinPoint joinPoint, String redisKey, Class<?> resultType, long expire, TimeUnit timeUnit) throws Throwable {
        //从redis获取
        List<?> objectList = redisUtils.getListFromString(redisKey, resultType);
        if (objectList != null) {
            return objectList;
        }

        Object result = joinPoint.proceed();

        //设置到redis
        redisUtils.setObjectJson(redisKey,result,expire,timeUnit);
        return result;
    }

    public static String overallKey(String prefix, ProceedingJoinPoint joinPoint){
        //获取所有KeyParam注解
        List<KeyParam> keyParams = AopUtils.paramAnnotationsFrom(joinPoint, KeyParam.class);
        //对应实参转String
        List<String> args = AopUtils.annotatedArgsFrom(joinPoint, KeyParam.class, String.class);
        //合并出整体key
        String suffix = StringUtils.concatForNotNull(":", args);
        return (suffix == null) ? prefix : prefix + suffix;
    }
}

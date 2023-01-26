package com.acimage.common.redis;


import com.acimage.common.redis.annotation.KeyParam;
import com.acimage.common.redis.annotation.QueryRedis;
import com.acimage.common.redis.enums.DataType;
import com.acimage.common.utils.*;
import com.acimage.common.utils.common.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Aspect
@Component
@Slf4j
public class QueryRedisAdvice {
    private static final String POINT_CUT_PATTERN = "@annotation(com.acimage.common.redis.annotation.QueryRedis)";
    @Autowired
    RedisUtils redisUtils;

    @Pointcut(POINT_CUT_PATTERN)
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        Method method = AopUtils.methodOf(joinPoint);
        QueryRedis queryRedis = AopUtils.annotationFrom(joinPoint, QueryRedis.class);
        Class<?> returnType = method.getReturnType();
        Class<?>[] generics = ReflectUtils.genericsOfReturnType(method);

        //获取@QueryRedis的属性
        String prefix = queryRedis.keyPrefix();
        TimeUnit timeUnit = queryRedis.unit();
        DataType dataType = queryRedis.dataType();

        //获取被注解的形参和实参
        List<KeyParam> annotatedParams = AopUtils.paramAnnotationsFrom(joinPoint, KeyParam.class);
        List<String> annotatedArgs = AopUtils.annotatedArgsFrom(joinPoint, KeyParam.class, String.class);
        //获取所有实参
        List<String> args = Arrays.stream(joinPoint.getArgs()).map(Object::toString).collect(Collectors.toList());
        //合并出整体key
        String suffix = StringUtils.concatForNotNull(":", args);
        String overallKey = prefix + suffix;

        //根据注解和实参信息得到起作用的expire
        long expire = getActiveExpire(queryRedis.expire(), annotatedParams, annotatedArgs);
        if (expire <= 0) {
            return joinPoint.proceed();
        }

        if (dataType == DataType.STRING) {
            return queryOrProceed(joinPoint, overallKey, expire, timeUnit, returnType, generics);
        } else if (dataType == DataType.HASH) {
            return queryOrProceedForHash(joinPoint, overallKey, expire, timeUnit, returnType);
        } else {
            throw new RuntimeException(this.getClass()+"到达不存在的分支");
        }
    }

    /**
     * 查询redis，有则返回，无则调用原方法被设置到redis并返回，针对返回值是list的类型
     */
    private Object queryOrProceed(ProceedingJoinPoint joinPoint, String redisKey, long expire, TimeUnit timeUnit, Class<?> returnType, @Nullable Class<?>... generics) throws Throwable {
        //从redis获取
        Object obj;
        Class<?>[] newGenerics = (generics == null) ? new Class<?>[]{} : generics;

        obj = redisUtils.getFromString(redisKey, returnType, newGenerics);

        if (obj != null) {
            return obj;
        }

        Object result = joinPoint.proceed();

        //设置到redis
        if (result != null) {
            redisUtils.setObjectJson(redisKey, result, expire, timeUnit);
        }

        return result;
    }

    private Object queryOrProceedForHash(ProceedingJoinPoint joinPoint, String redisKey, long expire, TimeUnit timeUnit, Class<?> returnType) throws Throwable {
        //从redis获取
        Object obj;

        obj = redisUtils.getObjectForHash(redisKey, returnType);

        if (obj != null) {
            return obj;
        }

        Object result = joinPoint.proceed();
        //设置到redis
        if (result != null) {
            redisUtils.setObjectForHash(redisKey, result,expire,timeUnit);
        }

        return result;
    }

    /**
     * 若当前实参值和@KeyParam中的specials匹配，则返回该参数注解的expire，否则返回default expire
     * @param annotatedArgs 被注解的实参值
     */
    private long getActiveExpire(long defaultExpire, List<KeyParam> keyParams, List<String> annotatedArgs) {
        assert keyParams.size() == annotatedArgs.size();

        //先判断当前被注解实参值和它们的@KeyParam注解中的spValues参数是否符合
        boolean isExpireChange = false;
        long expire = defaultExpire;

        for (int i = 0; i < keyParams.size(); i++) {

            String[] specials = keyParams.get(i).specials();
            long[] expires = keyParams.get(i).expires();
            //未设置specials则不用考虑
            if (specials.length == 0) {
                continue;
            }

            Integer index = ArrayUtils.firstIndexOf(specials, annotatedArgs.get(i));
            if (index == null) {
                //当前实参和@KeyParam注解中specials不匹配
                return defaultExpire;
            } else {

                if (expires.length <= index) {
                    log.error("@KeyParam注解使用错误：specials和expire长度不一致");
                } else {
                    if (!isExpireChange) {
                        expire = expires[index];
                        isExpireChange = true;
                    }

                }
            }
        }

        return expire;
    }
}

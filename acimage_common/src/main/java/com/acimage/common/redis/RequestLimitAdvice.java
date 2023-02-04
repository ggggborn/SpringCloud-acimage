package com.acimage.common.redis;


import com.acimage.common.redis.annotation.KeyParam;
import com.acimage.common.redis.annotation.QueryRedis;
import com.acimage.common.redis.annotation.RequestLimit;
import com.acimage.common.redis.enums.DataType;
import com.acimage.common.utils.common.AopUtils;
import com.acimage.common.utils.common.ArrayUtils;
import com.acimage.common.utils.common.ReflectUtils;
import com.acimage.common.utils.common.StringUtils;
import com.acimage.common.utils.redis.RedisUtils;
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
public class RequestLimitAdvice {
    private static final String POINT_CUT_PATTERN = "@annotation(com.acimage.common.redis.annotation.RequestLimit)";
    private static final String REQUEST_LIMIT="acimage:request:limit:";
    @Autowired
    RedisUtils redisUtils;

    @Pointcut(POINT_CUT_PATTERN)
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = AopUtils.methodOf(joinPoint);
        RequestLimit requestLimit = AopUtils.annotationFrom(joinPoint, RequestLimit.class);
        String methodName=method.getName();
        String className=method.getDeclaringClass().getName();
        String key=REQUEST_LIMIT+String.format("%s:%s",methodName,className);
        return joinPoint.proceed();
    }


}

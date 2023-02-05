package com.acimage.common.redis;



import com.acimage.common.global.context.UserContext;
import com.acimage.common.redis.annotation.RequestLimit;
import com.acimage.common.redis.enums.LimitTarget;
import com.acimage.common.result.Result;
import com.acimage.common.utils.common.*;
import com.acimage.common.utils.redis.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Aspect
@Component
@Slf4j
@Order(1)
public class RequestLimitAdvice {
    private static final String POINT_CUT_PATTERN = "@annotation(com.acimage.common.redis.annotation.RequestLimit)";
    private static final String STRINGKP_REQUEST_LIMIT = "acimage:request:limit:";
    @Autowired
    RedisUtils redisUtils;

    @Pointcut(POINT_CUT_PATTERN)
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = AopUtils.methodOf(joinPoint);
        RequestLimit requestLimit = AopUtils.annotationFrom(joinPoint, RequestLimit.class);
        String methodName = method.getName();
        String className = method.getDeclaringClass().getName();
        String predix = STRINGKP_REQUEST_LIMIT + String.format("%s:%s:", methodName, className);
        LimitTarget[] targets = requestLimit.targets();

        long[] limitTimes = requestLimit.limitTimes();
        long[] durations = requestLimit.durations();
        long[] penaltyTimes = requestLimit.penaltyTimes();
        TimeUnit timeUnit = requestLimit.unit();

        int len = targets.length;
        if (limitTimes.length != len || durations.length != len || penaltyTimes.length != len) {
            throw new IllegalArgumentException("注解参数长度不一致");
        }
        List<String> keys = new ArrayList<>();
        List<Long> limitList=new ArrayList<>();
        List<Long> durationList=new ArrayList<>();
        List<Long> penaltyList=new ArrayList<>();
        List<LimitTarget> validTargetList=new ArrayList<>();

        for (int i=0;i<targets.length;i++) {
            switch (targets[i]){
                case IP:
                    if(UserContext.getIp()!=null){
                        validTargetList.add(targets[i]);
                        keys.add(predix + String.format("%s:%s",targets[i],UserContext.getIp()));
                        limitList.add(toSeconds(limitTimes[i],timeUnit));
                        durationList.add(toSeconds(durations[i],timeUnit));
                        penaltyList.add(toSeconds(penaltyTimes[i],timeUnit));
                    }
                    break;
                case USER:
                    if(UserContext.getUserId()!=null){
                        validTargetList.add(targets[i]);
                        keys.add(predix + String.format("%s:%s",targets[i],UserContext.getUserId()));
                        limitList.add(toSeconds(limitTimes[i],timeUnit));
                        durationList.add(toSeconds(durations[i],timeUnit));
                        penaltyList.add(toSeconds(penaltyTimes[i],timeUnit));
                    }
                    break;
                case ALL:
                    validTargetList.add(targets[i]);
                    keys.add(predix +targets[i]);
                    limitList.add(toSeconds(limitTimes[i],timeUnit));
                    durationList.add(toSeconds(durations[i],timeUnit));
                    penaltyList.add(toSeconds(penaltyTimes[i],timeUnit));
                    break;
            }
        }
        List<Long> incrementResults = redisUtils.requestLimitScript(keys, limitList, durationList, penaltyList);
        for (int i = 0; i < incrementResults.size(); i++) {
            if (incrementResults.get(i) > limitList.get(i)) {
                switch (validTargetList.get(i)) {
                    case IP:
                    case USER:
                        return Result.fail("请勿频繁操作");
                    case ALL:
                        return Result.fail("系统繁忙，请稍后重试");
                }
            }
        }

        return joinPoint.proceed();
    }

    private List<Long> toSecondsList(long[] times, TimeUnit unit) {
        Long[] ts = new Long[times.length];
        for (int i = 0; i < times.length; i++) {
            ts[i] = times[i];
        }
        return Arrays.stream(ts).map(obj -> toSeconds(obj, unit)).collect(Collectors.toList());
    }

    private long toSeconds(long time, TimeUnit timeUnit) {
        long res = time;
        switch (timeUnit) {
            case DAYS:
                res = res * 24;
            case HOURS:
                res = res * 60;
            case MINUTES:
                res = res * 60;
            case SECONDS:
                return res;
        }
        throw new IllegalArgumentException("只支持timeUnit为day,hour,minute,second");
    }




}

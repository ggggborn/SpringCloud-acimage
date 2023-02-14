package com.acimage.common.global.aop;


import com.acimage.common.utils.common.AopUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component
@Slf4j
@Order(200)
public class LogAdvice {

    private static final int MAX_RETURN_VALUE_LENGTH = 100;

    //    @Pointcut("execution(public * com.acimage..*.controller.*Controller.*(..))")
    @Pointcut("execution(public * com.acimage..*.*Controller.*(..))")
    public void controllerPointCut() {
    }

    @Pointcut("execution(public * com.acimage..*.*Provider.*(..))")
    public void providerPointCut() {
    }

    /**
     * 记录每个接口出异常时时的入参
     */
    @Around("controllerPointCut() || providerPointCut()")
    private Object logParametersAndReturnValue(ProceedingJoinPoint joinPoint) throws Throwable {

        Method method = AopUtils.methodOf(joinPoint);
        Object[] args = joinPoint.getArgs();
        Parameter[] parameters = method.getParameters();

        long startTime = System.currentTimeMillis();

        //记录入参
        StringBuilder argsString = new StringBuilder();
        argsString.append(method.getName());
        argsString.append(" 入参-->");
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                if (i < parameters.length) {
                    argsString.append(parameters[i].getName());
                    argsString.append(": ");
                }

                argsString.append(args[i]);
                argsString.append(" ");
            }
        }

        Object obj;
        try {
            obj = joinPoint.proceed();
        } catch (Throwable e) {
            log.error("出异常: " + argsString);
            throw e;
        }

        if (!method.isAnnotationPresent(GetMapping.class)) {
            String returnValue = obj == null ? "" : obj.toString();
            log.info(method.getName() + " 返回值-->" + returnValue);
        }

        String classMethod = method.getName() + " " + method.getDeclaringClass().getSimpleName();
        long cost = System.currentTimeMillis() - startTime;
        if (cost > 500) {
            log.warn("{}耗时 {}ms", classMethod, cost);
        } else {
            log.debug("{}耗时 {}ms", classMethod, cost);
        }

        return obj;
    }
}

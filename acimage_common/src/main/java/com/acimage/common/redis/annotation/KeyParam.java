package com.acimage.common.redis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 和@QueryRedis配合使用，被注解的参数作为redis键的后缀，可以有多个
 * 但只能有一个KeyParam设置spValues和expire
 */
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface KeyParam {
    /**
     * 后缀取这些值时，过期时间根据本身的expire设置
     */
    String[] specials() default {};

    /**
     * 对应的过期时间
     */
    long[] expires() default {};
}

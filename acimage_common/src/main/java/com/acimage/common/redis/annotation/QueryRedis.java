package com.acimage.common.redis.annotation;

import com.acimage.common.redis.enums.DataType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryRedis {

    String keyPrefix() ;
    /**
     * expire<=0并且参数中没有@KeyParam设置了expire时，不会设置到redis，
     */
    long expire() default 1L;
    TimeUnit unit() default TimeUnit.MINUTES;
    DataType dataType() default DataType.STRING;

}

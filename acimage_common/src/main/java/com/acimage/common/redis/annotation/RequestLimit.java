package com.acimage.common.redis.annotation;


import com.acimage.common.redis.enums.LimitTarget;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLimit {

    int[] limitTimes();
    int[] durations();
    LimitTarget[] targets();
    TimeUnit[] units();
}

package com.acimage.community.service.comment.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 根据被注解的方法的返回值更改对应评论的话题数
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UpdateCcByReturn {
    Operation operation() default Operation.ADD;

}

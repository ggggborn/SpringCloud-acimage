package com.acimage.common.global.annotation;

import com.acimage.common.global.enums.AuthenticationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解上的类或方法表示不需要登录后才能访问
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Authentication {
    AuthenticationType type() default AuthenticationType.TOKEN_REQUIRED;

}

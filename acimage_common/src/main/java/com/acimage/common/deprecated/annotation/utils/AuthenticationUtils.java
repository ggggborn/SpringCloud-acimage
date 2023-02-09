package com.acimage.common.deprecated.annotation.utils;

import com.acimage.common.deprecated.annotation.Authentication;
import com.acimage.common.global.enums.AuthenticationType;
import org.springframework.web.method.HandlerMethod;


public class AuthenticationUtils {
    public static AuthenticationType getAuthenticationType(HandlerMethod handlerMethod){
        if(handlerMethod==null){
            return null;
        }
        //判断方法上是否有Authorization注解
        Authentication methodAuthentication =handlerMethod.getMethod().getAnnotation(Authentication.class);
        if(methodAuthentication !=null){
            return methodAuthentication.type();
        }
        //获取类上的Authorization注解
        Authentication classAuthentication =handlerMethod.getBeanType().getAnnotation(Authentication.class);
        if(classAuthentication !=null){
            return classAuthentication.type();
        }
        return null;
    }
}

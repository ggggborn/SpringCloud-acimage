package com.acimage.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtils implements ApplicationContextAware {
    /**
     * spring的应用上下文
     */
    private static ApplicationContext applicationContext;

    /**
     * 初始化时将应用上下文设置进applicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.applicationContext=applicationContext;
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }


    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }


    public static <T> T getBean(Class<T> beanClass) throws BeansException {
        return applicationContext.getBean(beanClass);
    }

    /**
     * 获取spring.profiles.active
     * @return
     */
    public static String getProfile(){
        return getApplicationContext().getEnvironment().getActiveProfiles()[0];
    }

    public static boolean isDev(){
        String[] profiles=getApplicationContext().getEnvironment().getActiveProfiles();
        for(String profile:profiles){
            if(profile.startsWith("dev")){
                return true;
            }
        }
        return false;
    }
}
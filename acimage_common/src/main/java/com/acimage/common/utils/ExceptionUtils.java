package com.acimage.common.utils;

public class ExceptionUtils {

    public static void printIfDev(Throwable e){
        if(SpringContextUtils.isDev()){
            e.printStackTrace();
        }
    }
}

package com.acimage.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

    public static String getValueByName(Cookie[] cookies,String name){
        if(cookies==null){
            return null;
        }
        for(int i=0;i< cookies.length;i++){
            if(cookies[i].getName().equals(name)){
                return cookies[i].getValue();
            }
        }
        return null;
    }
    public static Cookie createCookie(String key,String value){
        Cookie cookie = new Cookie(key,value);
        cookie.setPath("/");
        cookie.setMaxAge(-1);
        return cookie;
    }
    public static Cookie createCookie(String key,String value,boolean httpOnly){
            Cookie cookie = new Cookie(key,value);
            cookie.setPath("/");
            cookie.setMaxAge(-1);
            cookie.setHttpOnly(httpOnly);
            return cookie;
    }
    public static Cookie createCookie(String key,String value,String path,int expire){
        Cookie cookie = new Cookie(key,value);
        cookie.setPath(path);
        cookie.setMaxAge(expire);
        return cookie;
    }
}

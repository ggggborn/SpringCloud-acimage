package com.acimage.common.global.enums;

import org.springframework.http.HttpMethod;

public enum MyHttpMethod {
    GET,
    POST,
    PUT,
    DELETE,
    ALL;

    public static MyHttpMethod from(HttpMethod method) {
        try {
            return MyHttpMethod.valueOf(method.toString());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

package com.acimage.feign.config;


import com.acimage.common.global.context.UserContext;
import com.acimage.common.global.consts.RequestHeaderKey;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Slf4j
@Configuration
public class FeignRequestInterceptorConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attrs==null){
            log.error("feign请求为空");
            return;
        }
        HttpServletRequest request = attrs.getRequest();
        Enumeration<String> attributeNames = request.getHeaderNames();
        //设置header
        if (attributeNames != null) {
            while (attributeNames.hasMoreElements()) {
                String name = attributeNames.nextElement();
                String value = request.getHeader(name);
                requestTemplate.header(name,value);
//                String KEY_COOKIE="cookie";
//                if(KEY_COOKIE.equals(name)){
//                    requestTemplate.header(name,value);
//                    return;
//                }
            }
        }
        //往header设置用户原始ip
        requestTemplate.header(RequestHeaderKey.ORIGIN_USER_IP, UserContext.getIp());


    }
}

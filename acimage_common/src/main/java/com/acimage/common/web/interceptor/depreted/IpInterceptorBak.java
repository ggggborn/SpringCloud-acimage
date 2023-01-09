package com.acimage.common.web.interceptor.depreted;

import com.acimage.common.global.context.UserContext;
import com.acimage.common.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 获取请求的token状态
 */
@Slf4j
@Component
public class IpInterceptorBak implements HandlerInterceptor {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String KEY_IP="acimage:ip:";
        String KEY_BLACK_IP="acimage:ip:black";
        long EXPIRE_SECONDS=10L;
        long limitTimes=300L;

        String ip = IpUtils.getIp(request);
        UserContext.setIp(ip);
        String ipKey=KEY_IP+ip;
        String blackIpKey=KEY_BLACK_IP+ip;

        Boolean isBlackIp=stringRedisTemplate.opsForSet().isMember(blackIpKey,ip);

        if(Boolean.TRUE.equals(isBlackIp)){
            return false;
        }

        if (handler instanceof HandlerMethod) {
            Long visitNum=stringRedisTemplate.opsForValue().increment(ipKey);
            if(visitNum==null){
                log.error("ip:{} 操作：ip访问计数 错误：redis中ip计数返回值为空",ip);
            } else if (visitNum==1){
                stringRedisTemplate.expire(ipKey,EXPIRE_SECONDS, TimeUnit.SECONDS);
            } else if (visitNum>limitTimes) {
                //加入黑名单
                stringRedisTemplate.opsForSet().add(blackIpKey,ip);
                response.setStatus(HttpStatus.FORBIDDEN.value());
                return false;
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.remove();
    }
}

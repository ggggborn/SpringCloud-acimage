package com.acimage.gateway.globalfilter;


import com.acimage.common.global.exception.NullTokenException;
import com.acimage.common.global.consts.HeaderKeyConstants;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.service.impl.TokenServiceImpl;
import com.acimage.common.utils.IpUtils;
import com.acimage.common.utils.JwtUtils;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.gateway.config.IgnoreUrlConfig;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;


import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Order(20)
@Component
public class AuthenticationFilter implements GlobalFilter {

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedisUtils redisUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        String url = request.getURI().getPath();
        if (IgnoreUrlConfig.isIgnoreUrl(url)) {
            return chain.filter(exchange);
        }

        String token = request.getHeaders().getFirst(HeaderKeyConstants.AUTHORIZATION);
        String ip = IpUtils.getUserIp(request);
        boolean isException = false;
        //验证token
        try {
            JwtUtils.verifyToken(token);
        } catch (NullTokenException e) {
            log.info("access 无token 访问:{} ip:{}", url, ip);
            isException = true;

        } catch (TokenExpiredException e) {
            log.warn("access token过期 用户:{}  访问:{} ip:{}",
                    JwtUtils.getUsername(token), url, ip);
            isException = true;

        } catch (JWTVerificationException e1) {
            log.error("access 非法token 访问:{} ip:{}", url, ip);
            isException = true;
        }

        if (!isException) {
            if (!this.isMatch(token, ip)) {
                log.info("access token和ip不匹配 用户:{} 访问:{} ip:{}",
                        JwtUtils.getUsername(token), url, ip);
            }

            String method = request.getMethodValue();
            log.info("access 用户:{} 访问:{} {} ip:{}",
                    JwtUtils.getUsername(token), url, method, ip);

            UserContext.setUserId(JwtUtils.getUserId(token));
            UserContext.setUsername(JwtUtils.getUsername(token));
            UserContext.setIp(ip);
        }


        return chain.filter(exchange);

    }

    private boolean isMatch(String token, String ip) {
        if (ip == null) {
            return false;
        }
        return ip.equals(redisUtils.getForString(TokenServiceImpl.STRINGKP_TOKEN + token));
    }
}

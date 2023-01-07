package com.acimage.common.service.impl;

import com.acimage.common.global.consts.HeaderKeyConstants;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.service.TokenService;
import com.acimage.common.utils.CookieUtils;
import com.acimage.common.utils.JwtUtils;
import com.acimage.common.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Service
public class TokenServiceImpl implements TokenService {
    private static final String STRINGKP_TOKEN ="acimage:tokens:token:";
    private static class CookieKey {
        public static final String KEY_PHOTO_URL ="photoUrl";
        public static final String KEY_USER_ID="userId";
        public static final String KEY_USERNAME="username";
        public static final String KEY_TOKEN="token";
    }
    @Autowired
    RedisUtils redisUtils;
    @Override
    public String createTokenAndAddInfoInCookie(long userId, String username, String photoUrl, HttpServletResponse resp) {
        //生成token
        String token = JwtUtils.createToken(userId, username,photoUrl);

        //让浏览器将用户信息添加到Cookie中
        resp.addCookie(CookieUtils.createCookie(CookieKey.KEY_PHOTO_URL, photoUrl));
        resp.addCookie(CookieUtils.createCookie(CookieKey.KEY_USER_ID, Long.toString(userId)));
        resp.addCookie(CookieUtils.createCookie(CookieKey.KEY_USERNAME, username));
        resp.addCookie(CookieUtils.createCookie(CookieKey.KEY_TOKEN, token, true));

        //记录token和ip的对应
        record(token, UserContext.getIp());

        return token;
    }

    @Override
    public String createToken(long userId, String username, String photoUrl){
        //生成token
        String token = JwtUtils.createToken(userId, username,photoUrl);
        //记录token和ip的对应
        record(token, UserContext.getIp());

        return token;
    }

    @Override
    public void record(String token,String ip){
        long EXPIRE_DAYS=2L;
        redisUtils.setAsString(STRINGKP_TOKEN +token,ip,EXPIRE_DAYS, TimeUnit.DAYS);
    }

    @Override
    public Boolean isMatch(String token,String ip){
        if(ip==null){
            return false;
        }
        return ip.equals(redisUtils.getForString(STRINGKP_TOKEN +token));
    }

    @Override
    public void invalidate(String token) {
        redisUtils.delete(STRINGKP_TOKEN +token);
    }

    @Override
    public String getToken(HttpServletRequest request){
        return request.getHeader(HeaderKeyConstants.AUTHORIZATION);
    }
}

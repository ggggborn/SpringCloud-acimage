package com.acimage.common.service.impl;

import com.acimage.common.global.context.UserContext;
import com.acimage.common.service.TokenService;
import com.acimage.common.utils.JwtUtils;
import com.acimage.common.utils.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenServiceImpl implements TokenService {
    public static final String STRINGKP_TOKEN ="acimage:tokens:token:";

    @Autowired
    RedisUtils redisUtils;


    @Override
    public String createAndRecordToken(long userId, String username, String photoUrl,int expireDays){
        //生成token
        String token = JwtUtils.createToken(userId, username,photoUrl,expireDays);
        //记录token和ip的对应
        record(token, UserContext.getIp());

        return token;
    }

    @Override
    public void record(String token,String ip){
        long EXPIRE_DAYS=10L;
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


}

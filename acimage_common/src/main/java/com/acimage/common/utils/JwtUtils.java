package com.acimage.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.acimage.common.global.exception.NullTokenException;


import java.util.Date;

public class JwtUtils {
    /**
     * 过期时间10min
     */
    private static final int EXPIRE_DAYS = 10;
    private static final String JWT_SECRET="my-jwt-secret";
    private static final String KEY_USERNAME="username";
    private static final String KEY_USER_ID="userId";
    private static final String KEY_PHOTO_URL="photoUrl";

    public static String createToken(long userId,String username,String photoUrl) {

        return JWT.create()
                .withIssuedAt(new Date())    //发行时间
                .withExpiresAt(DateUtil.offsetDay(new Date(), EXPIRE_DAYS) ) //有效截止时间
                .withClaim(KEY_USER_ID, userId)    //载荷，存储不敏感的用户信息
                .withClaim(KEY_USERNAME, username)
                .withClaim(KEY_PHOTO_URL, photoUrl)
                .sign(Algorithm.HMAC256(JWT_SECRET));   //加密(摘要)
    }

    public static void verifyToken(String token) throws JWTVerificationException {
        if(StrUtil.isBlank(token)){
            throw new NullTokenException("token is null！");
        }
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(JWT_SECRET)).build();
        try {
            verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw e;
        }
    }

    public static Long getUserId(String token) throws JWTDecodeException {
        return JWT.decode(token).getClaim(KEY_USER_ID).asLong();
    }

    public static String getUsername(String token)throws JWTDecodeException{
        return JWT.decode(token).getClaim(KEY_USERNAME).asString();
    }

    public static String getPhotoUrl(String token)throws JWTDecodeException{
        return JWT.decode(token).getClaim(KEY_PHOTO_URL).asString();
    }

}

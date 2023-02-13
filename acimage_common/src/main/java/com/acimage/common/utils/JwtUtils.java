package com.acimage.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.acimage.common.global.consts.JwtConstants;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.acimage.common.global.exception.NullTokenException;


import java.util.Date;

public class JwtUtils {

    public static String createToken(long userId, String username, String photoUrl, int expireDays) {

        return JWT.create()
                .withIssuedAt(new Date())    //发行时间
                .withExpiresAt(DateUtil.offsetDay(new Date(), expireDays)) //有效截止时间
                .withClaim(JwtConstants.KEY_USER_ID, userId)    //载荷，存储不敏感的用户信息
                .withClaim(JwtConstants.KEY_USERNAME, username)
                .withClaim(JwtConstants.KEY_PHOTO_URL, photoUrl)
                .sign(Algorithm.HMAC256(JwtConstants.JWT_SECRET));   //加密(摘要)
    }

    public static void verifyToken(String token) throws JWTVerificationException {
        if (StrUtil.isBlank(token)) {
            throw new NullTokenException("token is null！");
        }
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(JwtConstants.JWT_SECRET)).build();

        verifier.verify(token);

    }

    public static Long getUserId(String token) throws JWTDecodeException {
        return JWT.decode(token).getClaim(JwtConstants.KEY_USER_ID).asLong();
    }

    public static String getUsername(String token) throws JWTDecodeException {
        return JWT.decode(token).getClaim(JwtConstants.KEY_USERNAME).asString();
    }

    public static String getPhotoUrl(String token) throws JWTDecodeException {
        return JWT.decode(token).getClaim(JwtConstants.KEY_PHOTO_URL).asString();
    }

    public static Date getExpire(String token) throws JWTDecodeException {
        return JWT.decode(token).getExpiresAt();
    }

}

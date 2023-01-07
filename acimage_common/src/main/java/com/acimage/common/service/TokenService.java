package com.acimage.common.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TokenService {
    @Deprecated
    String createTokenAndAddInfoInCookie(long userId, String username, String photoUrl, HttpServletResponse resp);

    Boolean isMatch(String token,String ip);

    String createToken(long userId, String username, String photoUrl);

    void record(String token, String ip);

    void invalidate(String token);

    String getToken(HttpServletRequest request);
}

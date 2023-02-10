package com.acimage.common.service;

public interface TokenService {


    Boolean isMatch(String token,String ip);

    String createAndRecordToken(long userId, String username, String photoUrl,int expireDays);

    void record(String token, String ip);

    void invalidate(String token);

}

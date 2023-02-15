package com.acimage.common.service;

public interface TokenService {


    Boolean hasRecorded(String token);

    String createAndRecordToken(long userId, String username, String photoUrl,int expireDays);

    void record(String token, int expireDays);

    void invalidate(String token);

}

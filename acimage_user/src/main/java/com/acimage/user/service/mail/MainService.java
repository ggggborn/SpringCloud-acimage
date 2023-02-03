package com.acimage.user.service.mail;

import java.util.concurrent.TimeUnit;

public interface MainService {
    void sendTextMailMessage(String to, String subject, String text);

    void sendVerifyCodeMailMessage(String to, String code, int timeoutMinute);
}

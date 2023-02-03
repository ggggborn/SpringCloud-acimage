package com.acimage.user.service.verify;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface VerifyCodeService {
    void writeCodeImageToResponseAndRecord(HttpServletRequest request, HttpServletResponse response);

    boolean verifyAndRemoveIfSuccess(HttpServletRequest request, String code);

    void sendVerifyCodeToEmail(String email,int length);

    boolean verifyEmailByVerifyCode(String email, String code);
}

package com.acimage.admin.service.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface VerifyCodeService {
    void writeCodeImageToResponseAndRecord(HttpServletRequest request, HttpServletResponse response);

    boolean verifyAndRemoveIfSuccess(HttpServletRequest request, String code);

}

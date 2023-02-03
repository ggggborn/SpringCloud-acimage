package com.acimage.user.service.user;



import com.acimage.user.model.request.UserLoginReq;
import com.acimage.user.model.request.UserRegisterReq;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface LoginService {

    String getPublicKey();

    @Transactional
    String registerUser(UserRegisterReq userRegister);

    @Transactional
    String login(UserLoginReq userLogin);


    void logout(HttpServletRequest request);

}

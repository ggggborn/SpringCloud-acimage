package com.acimage.community.service.userclient.impl;


import com.acimage.common.exception.BusinessException;
import com.acimage.common.model.domain.user.User;
import com.acimage.common.result.Result;
import com.acimage.community.service.userclient.UserClientService;
import com.acimage.feign.client.UserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
public class UserClientServiceImpl implements UserClientService {
    @Autowired
    UserClient userClient;

    @Override
    public User queryUser(long userId) {
        Result<User> result= null;
        try {
            result = userClient.queryUser(userId);
        } catch (Exception e) {
            return new User();
        }

        log.info("{}",result);
        if(result.isOk()){
            return result.getData();
        }else{
            throw new BusinessException(result.getMsg());
        }
    }
}

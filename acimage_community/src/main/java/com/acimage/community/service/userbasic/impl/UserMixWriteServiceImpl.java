package com.acimage.community.service.userbasic.impl;

import com.acimage.common.model.domain.UserBasic;
import com.acimage.community.service.userbasic.UserBasicWriteService;
import com.acimage.community.service.userbasic.UserMixWriteService;
import com.acimage.community.service.userstatistic.UserCsWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMixWriteServiceImpl implements UserMixWriteService {
    @Autowired
    UserBasicWriteService userBasicWriteService;
    @Autowired
    UserCsWriteService userCsWriteService;

    @Override
    public void addUserBasicAndUserCommunityStatistic(UserBasic userBasic){
        userBasicWriteService.saveUser(userBasic);
        userCsWriteService.save(userBasic.getId());
    }
}

package com.acimage.community.service.cmtyuser.impl;

import com.acimage.common.model.domain.community.CmtyUser;
import com.acimage.community.service.cmtyuser.CmtyUserWriteService;
import com.acimage.community.service.cmtyuser.UserMixWriteService;
import com.acimage.community.service.userstatistic.UserCsWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


public class UserMixWriteServiceImpl implements UserMixWriteService {
    @Autowired
    CmtyUserWriteService cmtyUserWriteService;
    @Autowired
    UserCsWriteService userCsWriteService;

    @Override
    public void addUserBasicAndUserCommunityStatistic(CmtyUser cmtyUser){
        cmtyUserWriteService.save(cmtyUser);
        userCsWriteService.save(cmtyUser.getId());
    }
}
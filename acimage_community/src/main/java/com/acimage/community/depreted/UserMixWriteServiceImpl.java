package com.acimage.community.depreted;

import com.acimage.common.model.domain.community.CmtyUser;
import com.acimage.community.service.cmtyuser.CmtyUserWriteService;
import com.acimage.community.depreted.UserMixWriteService;
import com.acimage.community.depreted.userstatistic.UserCsWriteService;
import org.springframework.beans.factory.annotation.Autowired;


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

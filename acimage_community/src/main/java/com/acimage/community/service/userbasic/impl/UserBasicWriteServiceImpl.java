package com.acimage.community.service.userbasic.impl;

import com.acimage.community.dao.UserBasicDao;
import com.acimage.common.model.domain.community.UserBasic;
import com.acimage.community.service.userbasic.UserBasicWriteService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserBasicWriteServiceImpl implements UserBasicWriteService {
    @Autowired
    UserBasicDao userBasicDao;

    @Override
    public void updateUsername(long userId, String username) {
        LambdaUpdateWrapper<UserBasic> uw = new LambdaUpdateWrapper<>();
        uw.set(UserBasic::getUsername, username)
                .eq(UserBasic::getId, userId);
        userBasicDao.update(null, uw);
    }

    @Override
    public void updatePhotoUrl(long userId, String photoUrl) {
        LambdaUpdateWrapper<UserBasic> uw = new LambdaUpdateWrapper<>();
        uw.set(UserBasic::getPhotoUrl, photoUrl)
                .eq(UserBasic::getId, userId);
        userBasicDao.update(null, uw);
    }

    @Override
    public void saveUser(UserBasic userBasic) {
        userBasicDao.insert(userBasic);
    }
}

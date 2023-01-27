package com.acimage.user.service.user;


import com.acimage.common.model.domain.user.User;
import com.acimage.user.model.vo.ProfileVo;


public interface UserInfoService {
    ProfileVo getProfile();
    User getUserWithStarCount(long userId);

}

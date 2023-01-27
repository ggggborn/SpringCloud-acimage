package com.acimage.user.service.user;

import com.acimage.common.model.domain.user.User;

import java.util.List;

public interface UserRankService {

    List<User> pageUsersRankByStarCount();
}

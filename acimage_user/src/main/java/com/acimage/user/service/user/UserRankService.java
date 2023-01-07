package com.acimage.user.service.user;

import com.acimage.common.model.domain.User;

import java.util.List;

public interface UserRankService {

    List<User> pageUsersRankByStarCount();
}

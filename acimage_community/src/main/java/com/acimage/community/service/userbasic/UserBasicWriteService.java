package com.acimage.community.service.userbasic;

import com.acimage.common.model.domain.community.UserBasic;
import org.springframework.transaction.annotation.Transactional;

public interface UserBasicWriteService {
    void updateUsername(long userId, String username);

    void updatePhotoUrl(long userId, String photoUrl);

    @Transactional
    void saveUser(UserBasic userBasic);
}

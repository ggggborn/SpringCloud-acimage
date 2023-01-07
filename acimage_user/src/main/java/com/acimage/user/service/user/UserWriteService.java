package com.acimage.user.service.user;

import com.acimage.common.model.domain.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface UserWriteService {
    @Transactional
    String updateUsername(String username, HttpServletResponse resp);

    @Deprecated
    @Transactional(rollbackFor = Exception.class)
    String uploadPhotoAndUpdatePhotoUrl(MultipartFile photoFile, HttpServletResponse resp);


    String updatePhotoUrl(String newPhotoUrl);


}

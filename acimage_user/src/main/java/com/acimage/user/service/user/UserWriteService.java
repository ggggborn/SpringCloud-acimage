package com.acimage.user.service.user;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface UserWriteService {
    @Transactional
    String updateUsername(String username);

    @Deprecated
    @Transactional(rollbackFor = Exception.class)
    String uploadPhotoAndUpdatePhotoUrl(MultipartFile photoFile);


    String updatePhotoUrl(String newPhotoUrl);


}

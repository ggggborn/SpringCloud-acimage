package com.acimage.admin.service.api;

import com.acimage.admin.model.request.ApiAddReq;
import com.acimage.admin.model.request.ApiModifyReq;

public interface ApiWriteService {
    void save(ApiAddReq apiAddReq);

    void update(ApiModifyReq apiModifyReq);

    void delete(int id);
}

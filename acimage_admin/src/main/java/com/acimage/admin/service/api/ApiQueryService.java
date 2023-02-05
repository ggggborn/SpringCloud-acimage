package com.acimage.admin.service.api;

import com.acimage.admin.model.request.ApiQueryReq;
import com.acimage.common.model.domain.sys.Api;
import com.acimage.common.model.page.MyPage;

public interface ApiQueryService {
    MyPage<Api> pageBy(ApiQueryReq apiQueryReq);
}

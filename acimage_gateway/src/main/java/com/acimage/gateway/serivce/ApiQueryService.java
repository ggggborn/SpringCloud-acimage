package com.acimage.gateway.serivce;

import com.acimage.common.global.enums.MatchRule;
import com.acimage.common.model.domain.sys.Api;

import java.util.List;

public interface ApiQueryService {
    List<Api> listEnableApisBy(MatchRule matchRule);
}

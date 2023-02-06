package com.acimage.gateway.serivce.impl;

import com.acimage.common.global.enums.MatchRule;
import com.acimage.common.model.domain.sys.Api;
import com.acimage.gateway.dao.ApiDao;
import com.acimage.gateway.serivce.ApiQueryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.List;

@Service
public class ApiQueryQueryServiceImpl implements ApiQueryService {
    @Autowired
    ApiDao apiDao;

    @Override
    public List<Api> listEnableApisBy(@Nullable MatchRule matchRule) {
        LambdaQueryWrapper<Api> qw = new LambdaQueryWrapper<>();
        qw.eq(Api::isEnable, true);
        if (matchRule != null) {
            qw.eq(Api::getMatchRule, matchRule);
        }
        return apiDao.selectList(qw);
    }
}

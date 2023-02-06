package com.acimage.gateway.serivce.impl;

import com.acimage.common.model.domain.sys.UserRole;
import com.acimage.common.redis.annotation.QueryRedis;
import com.acimage.common.utils.common.ListUtils;
import com.acimage.gateway.dao.UserRoleDao;
import com.acimage.gateway.serivce.UserRoleQueryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleQueryQueryServiceImpl implements UserRoleQueryService {
    @Autowired
    UserRoleDao userRoleDao;

    @QueryRedis(keyPrefix = "acimage:gateway:permissionIds:userId:",expire = 5L)
    @Override
    public List<Integer> listRoleIds(long userId){
        LambdaQueryWrapper<UserRole> qw=new LambdaQueryWrapper<>();
        qw.select(UserRole::getRoleId)
                .eq(UserRole::getUserId,userId);
        return ListUtils.extract(UserRole::getRoleId,userRoleDao.selectList(qw));
    }
}

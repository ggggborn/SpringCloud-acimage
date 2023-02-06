package com.acimage.gateway.serivce.impl;

import com.acimage.common.model.domain.sys.Role;
import com.acimage.common.utils.common.ListUtils;
import com.acimage.gateway.dao.RoleDao;
import com.acimage.gateway.serivce.RoleQueryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleQueryServiceImpl implements RoleQueryService {
    @Autowired
    RoleDao roleDao;

    @Override
    public List<Integer> listAllIds(){
        LambdaQueryWrapper<Role> qw=new LambdaQueryWrapper<>();
        qw.select(Role::getId);
        List<Role> roles= roleDao.selectList(qw);
        return ListUtils.extract(Role::getId,roles);
    }
}

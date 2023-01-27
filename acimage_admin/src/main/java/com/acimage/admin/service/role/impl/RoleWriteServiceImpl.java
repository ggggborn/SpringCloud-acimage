package com.acimage.admin.service.role.impl;

import cn.hutool.core.bean.BeanUtil;
import com.acimage.admin.dao.sys.RoleDao;
import com.acimage.admin.model.request.RoleAddReq;
import com.acimage.admin.model.request.RoleModifyReq;
import com.acimage.admin.service.role.RoleWriteService;
import com.acimage.common.model.domain.sys.Role;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@DS("sys")
public class RoleWriteServiceImpl implements RoleWriteService {
    @Autowired
    RoleDao roleDao;

    @Override
    public void save(RoleAddReq roleAddReq) {
        Role role = new Role();
        BeanUtil.copyProperties(roleAddReq,role,false);
        roleDao.insert(role);
    }

    @Override
    public void remove(long id) {
        roleDao.deleteById(id);
    }

    @Override
    public void update(RoleModifyReq roleModifyReq) {
        LambdaUpdateWrapper<Role> uw = new LambdaUpdateWrapper<>();
        uw.eq(Role::getId, roleModifyReq.getId())
                .set(Role::getRoleName, roleModifyReq.getRoleName())
                .set(Role::getNote, roleModifyReq.getNote())
                .set(Role::getUpdateTime, new Date());
        roleDao.update(null, uw);
    }
}

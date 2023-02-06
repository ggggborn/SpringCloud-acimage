package com.acimage.admin.service.userrole.impl;

import com.acimage.admin.dao.sys.UserRoleDao;
import com.acimage.admin.global.consts.ModuleConstants;
import com.acimage.admin.service.user.UserQueryService;
import com.acimage.admin.service.userrole.UserRoleQueryService;
import com.acimage.admin.service.userrole.UserRoleWriteService;
import com.acimage.common.exception.BusinessException;
import com.acimage.common.model.domain.sys.UserRole;
import com.acimage.common.utils.IdGenerator;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@DS(ModuleConstants.SYS)
@Service
public class UserRoleWriteServiceImpl implements UserRoleWriteService {

    @Autowired
    UserRoleDao userRoleDao;
    @Autowired
    UserRoleQueryService userRoleQueryService;
    @Autowired
    UserQueryService userQueryService;

    @Override
    public void save(long userId, int roleId) {
        if (userRoleQueryService.getUserRole(userId, roleId) != null) {
            throw new BusinessException("该用户已有该角色");
        }

        if (userQueryService.getUser(userId) == null) {
            throw new BusinessException("该用户不存在");
        }

        long id = IdGenerator.getSnowflakeNextId();
        UserRole userRole = new UserRole(id, userId, roleId, new Date());
        userRoleDao.insert(userRole);
    }

    @Override
    public void remove(long userId, int roleId) {
        LambdaQueryWrapper<UserRole> qw=new LambdaQueryWrapper<>();
        qw.eq(UserRole::getUserId,userId)
                .eq(UserRole::getRoleId,roleId);
        userRoleDao.delete(qw);
    }
}

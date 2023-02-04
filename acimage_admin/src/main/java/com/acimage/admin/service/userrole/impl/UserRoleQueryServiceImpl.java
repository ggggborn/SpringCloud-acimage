package com.acimage.admin.service.userrole.impl;

import com.acimage.admin.dao.user.UserRoleDao;
import com.acimage.admin.global.consts.ModuleConstants;
import com.acimage.admin.service.userrole.UserRoleQueryService;
import com.acimage.common.model.domain.sys.UserRole;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@DS(ModuleConstants.SYS)
public class UserRoleQueryServiceImpl implements UserRoleQueryService {

    @Autowired
    UserRoleDao userRoleDao;

    @Override
    public Map<Long, List<Integer>> listUserIdWithRoleIds(List<Long> userIds) {
        LambdaQueryWrapper<UserRole> qw = new LambdaQueryWrapper<>();
        qw.in(UserRole::getUserId, userIds);
        List<UserRole> userRoles = userRoleDao.selectList(qw);

        //组装
        Map<Long, List<Integer>> map = new HashMap<>();
        for (UserRole userRole : userRoles) {
            long userId = userRole.getUserId();
            List<Integer> roleIds = map.get(userId);
            if (roleIds == null) {
                roleIds = new ArrayList<>();
                roleIds.add(userRole.getRoleId());
                map.put(userId,roleIds);
            }else{
                roleIds.add(userRole.getRoleId());
            }
        }
        return map;
    }

    @Override
    public UserRole getUserRole(long userId, int roleId){
        LambdaQueryWrapper<UserRole> qw=new LambdaQueryWrapper<>();
        qw.eq(UserRole::getUserId,userId)
                .eq(UserRole::getRoleId,roleId);
        return userRoleDao.selectOne(qw);
    }
}

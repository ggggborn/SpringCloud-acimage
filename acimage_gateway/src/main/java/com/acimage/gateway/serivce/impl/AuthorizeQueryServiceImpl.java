package com.acimage.gateway.serivce.impl;

import com.acimage.common.model.domain.sys.Authorize;
import com.acimage.common.utils.common.ListUtils;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.gateway.dao.AuthorizeDao;
import com.acimage.gateway.serivce.AuthorizeQueryService;
import com.acimage.gateway.serivce.RoleQueryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AuthorizeQueryServiceImpl implements AuthorizeQueryService {
    @Autowired
    AuthorizeDao authorizeDao;

    private Map<Integer, List<Integer>> rolePermissionIdsMap;
    @Override
    public Map<Integer, List<Integer>> getRolePermissionIdsMap() {
        return rolePermissionIdsMap;
    }

    @Override
    public List<Integer> listPermissionIds(Integer roleId) {
        LambdaQueryWrapper<Authorize> qw = new LambdaQueryWrapper<>();
        qw.eq(Authorize::getRoleId, roleId)
                .select(Authorize::getPermissionId);
        return ListUtils.extract(Authorize::getPermissionId, authorizeDao.selectList(qw));
    }

    @Override
    public List<Authorize> listAll() {
        return authorizeDao.selectList(null);
    }

    @Scheduled(fixedRate = 10L, timeUnit = TimeUnit.MINUTES)
    private Map<Integer, List<Integer>> refreshRoleIdToPermissionIdsMap() {
        //为了方便直接保存在内存
        Map<Integer, List<Integer>> map = new HashMap<>();
        List<Authorize> authorizeList = authorizeDao.selectList(null);
        for (Authorize authorize : authorizeList) {
            Integer roleId = authorize.getRoleId();
            Integer permissionId = authorize.getPermissionId();
            List<Integer> permissionIds = map.get(roleId);
            if (permissionIds == null) {
                List<Integer> tempList = new ArrayList<>();
                tempList.add(permissionId);
                map.put(roleId, tempList);
            } else {
                permissionIds.add(permissionId);
            }
        }
        this.rolePermissionIdsMap = map;
        return map;
    }
}

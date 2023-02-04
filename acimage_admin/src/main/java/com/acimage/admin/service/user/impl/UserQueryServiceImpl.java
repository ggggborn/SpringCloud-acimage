package com.acimage.admin.service.user.impl;


import com.acimage.admin.dao.user.UserDao;
import com.acimage.admin.global.consts.ModuleConstants;
import com.acimage.admin.model.request.UserQueryReq;
import com.acimage.admin.service.user.UserQueryService;
import com.acimage.admin.service.userrole.UserRoleQueryService;
import com.acimage.common.model.domain.user.User;
import com.acimage.common.model.page.MyPage;
import com.acimage.common.utils.common.ListUtils;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@DS(ModuleConstants.USER)
public class UserQueryServiceImpl implements UserQueryService {
    @Autowired
    UserDao userDao;
    @Autowired
    UserRoleQueryService userRoleQueryService;

    @Override
    public MyPage<User> listBy(UserQueryReq userQueryReq) {
        IPage<User> page = new Page<>(userQueryReq.getPageNo(), userQueryReq.getPageSize());
        String like = userQueryReq.getKeyword();


        IPage<User> resultPage;
        if (like != null && like.trim().length() > 0) {
            LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
            qw.like(User::getUsername, userQueryReq.getKeyword());
            page = userDao.selectPage(page, qw);
            resultPage = userDao.selectPage(page, qw);
        } else {
            resultPage = userDao.selectPage(page, null);
        }

        List<User> userList = resultPage.getRecords();
        int totalCount = (int) resultPage.getTotal();
        Map<Long, List<Integer>> map = userRoleQueryService.listUserIdWithRoleIds(ListUtils.extract(User::getId, userList));
        for (User user : userList) {
            List<Integer> roleIds = map.get(user.getId());
            if (roleIds == null) {
                user.setRoleIds(new ArrayList<>());
            } else {
                user.setRoleIds(roleIds);
            }
        }

        return new MyPage<>(totalCount, userList);
    }

    @Override
    public User getUser(long userId){
        return userDao.selectById(userId);
    }
}

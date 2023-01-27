package com.acimage.admin.service.role.impl;

import com.acimage.admin.dao.sys.RoleDao;
import com.acimage.admin.service.role.RoleQueryService;
import com.acimage.common.model.domain.sys.Role;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("sys")
public class RoleQueryServiceImpl implements RoleQueryService {
    @Autowired
    RoleDao roleDao;

    @Override
    public List<Role> listAll(){
        return roleDao.selectList(null);
    }
}

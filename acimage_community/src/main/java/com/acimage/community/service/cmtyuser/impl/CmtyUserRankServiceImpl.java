package com.acimage.community.service.cmtyuser.impl;

import com.acimage.common.model.domain.community.CmtyUser;
import com.acimage.common.model.page.MyPage;
import com.acimage.common.redis.annotation.QueryRedis;
import com.acimage.common.utils.LambdaUtils;
import com.acimage.common.utils.common.PageUtils;
import com.acimage.community.dao.CmtyUserDao;
import com.acimage.community.service.cmtyuser.CmtyUserRankService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CmtyUserRankServiceImpl implements CmtyUserRankService {
    @Autowired
    CmtyUserDao cmtyUserDao;

    private List<CmtyUser> pageUserRankBy(SFunction<CmtyUser, Integer> attr, int pageNo, int pageSize) {
        int start = PageUtils.startIndexOf(pageNo, pageSize);
        String column = LambdaUtils.underlineColumnNameOf(attr);
        List<CmtyUser> cmtyUserList = cmtyUserDao.selectListOrderByColumn(column, start, pageSize);
        return cmtyUserList;
    }

    @Override
    public MyPage<CmtyUser> pageUserRankBy(String column, int pageNo, int pageSize) {
        String underlineColumn = StringUtils.camelToUnderline(column);

        IPage<CmtyUser> page = new Page<>(pageNo, pageSize);
        QueryWrapper<CmtyUser> qw = new QueryWrapper<>();
        qw.orderByDesc(underlineColumn);

        return MyPage.from(cmtyUserDao.selectPage(page, qw));

    }

    @Override
    @QueryRedis(keyPrefix = "acimage:community:users:rank:topicCount:", expire = 5L)
    public List<CmtyUser> pageUserRankByTopicCount(int pageNo, int pageSize) {
        return pageUserRankBy(CmtyUser::getTopicCount, pageNo, pageSize);
    }

    @Override
    @QueryRedis(keyPrefix = "acimage:community:users:rank:starCount:", expire = 5L)
    public List<CmtyUser> pageUserRankByStarCount(int pageNo, int pageSize) {
        return pageUserRankBy(CmtyUser::getStarCount, pageNo, pageSize);
    }
}

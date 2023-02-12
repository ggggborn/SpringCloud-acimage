package com.acimage.community.depreted.userstatistic.impl;

import com.acimage.common.model.domain.user.User;
import com.acimage.common.deprecated.UserCommunityStatistic;
import com.acimage.common.redis.annotation.QueryRedis;
import com.acimage.common.utils.LambdaUtils;
import com.acimage.common.utils.common.PageUtils;
import com.acimage.community.depreted.CmtyUserDaoBak;
import com.acimage.community.depreted.userstatistic.UserCsRankService;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public class UserCsRankServiceImpl implements UserCsRankService {

    @Autowired
    CmtyUserDaoBak userCsDao;


    private List<User> pageUserRankBy(SFunction<UserCommunityStatistic, Integer> attr, int pageNo, int pageSize){
        int start= PageUtils.startIndexOf(pageNo,pageSize);
        String column= LambdaUtils.underlineColumnNameOf(attr);
        List<UserCommunityStatistic> userCsList= userCsDao.selectListOrderByColumn(column,start,pageSize);
        List<User> users =new ArrayList<>();
        for(UserCommunityStatistic userCs:userCsList){
            User user =new User();
            user.setTopicCount(userCs.getTopicCount());
            user.setStarCount(userCs.getStarCount());
            user.setId(userCs.getUserId());
            user.setUsername(userCs.getCmtyUser().getUsername());
            user.setPhotoUrl(userCs.getCmtyUser().getPhotoUrl());

            users.add(user);
        }
        return users;
    }

    @Override
    @QueryRedis(keyPrefix = "acimage:community:users:rank:topicCount:",expire = 5L)
    public List<User> pageUserRankByTopicCount(int pageNo, int pageSize){
        return pageUserRankBy(UserCommunityStatistic::getTopicCount,pageNo,pageSize);
    }

    @Override
    @QueryRedis(keyPrefix = "acimage:community:users:rank:starCount:",expire = 5L)
    public List<User> pageUserRankByStarCount(int pageNo, int pageSize){
        return pageUserRankBy(UserCommunityStatistic::getStarCount,pageNo,pageSize);
    }
}

package com.acimage.community.service.userstatistic.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Pair;
import com.acimage.common.deprecated.UserCommunityStatistic;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.common.utils.common.PairUtils;
import com.acimage.community.global.CmtyUserDaoBak;
import com.acimage.community.service.userstatistic.UserCsWriteService;
import com.acimage.community.service.userstatistic.consts.KeyConstants;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class UserCsWriteServiceImpl implements UserCsWriteService {
    @Autowired
    CmtyUserDaoBak userCsDao;
    @Autowired
    RedisUtils redisUtils;

    @Override
    public Integer updateStarCountByIncrements(List<Long> userIds, List<Integer> starCounts){
        List<Pair<Long,Integer>> userIdAndStarCounts= PairUtils.combine(userIds,starCounts);
        if(CollectionUtil.isEmpty(userIds)){
            return 0;
        }
        return userCsDao.batchUpdateStarCount(userIdAndStarCounts);
    }

    @Override
    public Integer updateTopicCountByIncrements(List<Long> userIds, List<Integer> starCounts){
        List<Pair<Long,Integer>> userIdAndStarCounts= PairUtils.combine(userIds,starCounts);
        if(CollectionUtil.isEmpty(userIds)){
            return 0;
        }
        return userCsDao.batchUpdateTopicCount(userIdAndStarCounts);
    }

    @Override
    public Integer updateTopicCountByIncrement( long userId, int increment){
        redisUtils.delete(KeyConstants.STRINGKP_USER_STATISTIC+userId);
        return userCsDao.updateTopicCountByIncrement(userId,increment);
    }

    @Override
    public Integer updateStarCountByIncrement(long userId, int increment) {
        redisUtils.delete(KeyConstants.STRINGKP_USER_STATISTIC+userId);
        return userCsDao.updateStarCountByIncrement(userId,increment);
    }

    @Override
    public void save(long userId){
        UserCommunityStatistic userCs=new UserCommunityStatistic();
        userCs.setUserId(userId);
        userCsDao.insert(userCs);
    }
}

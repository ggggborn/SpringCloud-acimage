package com.acimage.community.service.cmtyuser.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Pair;
import com.acimage.common.model.domain.community.CmtyUser;
import com.acimage.common.utils.LambdaUtils;
import com.acimage.common.utils.common.PairUtils;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.community.dao.CmtyUserDao;
import com.acimage.community.service.cmtyuser.CmtyUserWriteService;
import com.acimage.community.depreted.userstatistic.consts.KeyConstants;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CmtyUserWriteServiceImpl implements CmtyUserWriteService {
    @Autowired
    CmtyUserDao cmtyUserDao;
    @Autowired
    RedisUtils redisUtils;

    @Override
    public void updateUsername(long userId, String username) {
        LambdaUpdateWrapper<CmtyUser> uw = new LambdaUpdateWrapper<>();
        uw.set(CmtyUser::getUsername, username)
                .eq(CmtyUser::getId, userId);
        cmtyUserDao.update(null, uw);
        redisUtils.delete(KeyConstants.STRINGKP_CMTY_USER);
    }

    @Override
    public void updatePhotoUrl(long userId, String photoUrl) {
        LambdaUpdateWrapper<CmtyUser> uw = new LambdaUpdateWrapper<>();
        uw.set(CmtyUser::getPhotoUrl, photoUrl)
                .eq(CmtyUser::getId, userId);
        cmtyUserDao.update(null, uw);
        redisUtils.delete(KeyConstants.STRINGKP_CMTY_USER);
    }

    @Override
    public void save(CmtyUser cmtyUser) {
        cmtyUserDao.insert(cmtyUser);
    }


    @Override
    public Integer updateStarCountByIncrements(List<Long> userIds, List<Integer> starCounts){
        List<Pair<Long,Integer>> userIdAndStarCounts= PairUtils.combine(userIds,starCounts);
        if(CollectionUtil.isEmpty(userIds)){
            return 0;
        }
        return cmtyUserDao.batchUpdateStarCount(userIdAndStarCounts);
    }

    @Override
    public Integer updateTopicCountByIncrements(List<Long> userIds, List<Integer> starCounts){
        List<Pair<Long,Integer>> userIdAndStarCounts= PairUtils.combine(userIds,starCounts);
        if(CollectionUtil.isEmpty(userIds)){
            return 0;
        }
        return cmtyUserDao.batchUpdateTopicCount(userIdAndStarCounts);
    }

    @Override
    public Integer updateTopicCountByIncrement( long userId, int increment){
        redisUtils.delete(KeyConstants.STRINGKP_CMTY_USER +userId);
        return cmtyUserDao.updateTopicCountByIncrement(userId,increment);
    }

    @Override
    public Integer updateStarCountByIncrement(long userId, int increment) {
        int col=cmtyUserDao.updateStarCountByIncrement(userId,increment);
        String key=KeyConstants.STRINGKP_CMTY_USER +userId;
        String column= LambdaUtils.columnOf(CmtyUser::getStarCount);
        redisUtils.incrementIfPresentForFieldKey(key,column,increment);
        return col;
    }
}

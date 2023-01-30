package com.acimage.community.service.cmtyuser;

import com.acimage.common.model.domain.community.CmtyUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CmtyUserWriteService {
    void updateUsername(long userId, String username);

    void updatePhotoUrl(long userId, String photoUrl);

    @Transactional
    void save(CmtyUser cmtyUser);

    Integer updateStarCountByIncrements(List<Long> userIds, List<Integer> starCounts);

    Integer updateTopicCountByIncrements(List<Long> userIds, List<Integer> starCounts);

    Integer updateTopicCountByIncrement(long userId, int increment);

    Integer updateStarCountByIncrement(long userId, int increment);
}

package com.acimage.community.service.star;

import com.acimage.common.model.domain.community.Star;
import com.acimage.common.model.page.MyPage;

public interface StarMixQueryService {
    MyPage<Star> pageStarsWithTopic(long userId, int pageNo);
}

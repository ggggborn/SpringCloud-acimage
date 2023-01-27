package com.acimage.community.service.star;

import com.acimage.common.model.domain.community.Star;
import com.acimage.common.model.page.Page;

public interface StarMixQueryService {
    Page<Star> pageStarsWithTopic(long userId, int pageNo);
}

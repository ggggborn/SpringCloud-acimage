package com.acimage.community.service.star;

import com.acimage.common.model.domain.Star;
import com.acimage.common.model.page.Page;

public interface StarMixQueryService {
    Page<Star> pageStarsWithTopic(long userId, int pageNo);
}

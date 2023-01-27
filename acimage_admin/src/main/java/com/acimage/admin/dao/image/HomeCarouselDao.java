package com.acimage.admin.dao.image;


import com.acimage.common.model.domain.image.HomeCarousel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface HomeCarouselDao extends BaseMapper<HomeCarousel> {
    @Select("select coalesce(max(location),0) from tb_home_carousel")
    Integer getMaxLocation();

    List<HomeCarousel> count();
}

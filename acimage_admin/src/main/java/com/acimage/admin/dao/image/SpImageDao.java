package com.acimage.admin.dao.image;


import com.acimage.common.global.enums.ImageType;
import com.acimage.common.model.domain.SpImage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface SpImageDao extends BaseMapper<SpImage> {
    @Select("select coalesce(max(location),0) from tb_sp_image where type=#{imageType}")
    Integer getMaxLocation(@Param("imageType") ImageType imageType);

    List<SpImage> count();
}

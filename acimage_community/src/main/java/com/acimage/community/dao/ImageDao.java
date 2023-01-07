package com.acimage.community.dao;

import cn.hutool.core.lang.Pair;
import com.acimage.common.model.domain.Image;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ImageDao extends BaseMapper<Image> {

    Integer insertList(List<Image> images);

    Integer updateDescription(List<Pair<Long,String>> idAndDescriptions);

    @Select("select * from tb_image where topic_id=#{topicId} order by id")
    List<Image> selectListOrderById(@Param("topicId") long topicId);


    List<Image> selectImagesWithTopic(List<Long> imageIds);
}

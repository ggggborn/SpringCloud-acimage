package com.acimage.community.dao;

import cn.hutool.core.lang.Pair;
import com.acimage.common.model.domain.community.CmtyUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CmtyUserDao extends BaseMapper<CmtyUser> {

//    @Select("select * from tb_user_basic where id=#{id}")
//    CmtyUser selectCmtyUserById(@Param("id") long id);

    Integer batchUpdateStarCount(@Param("userIdAndIncrements") List<Pair<Long,Integer>> userIdAndIncrements);
    Integer batchUpdateTopicCount(@Param("userIdAndIncrements") List<Pair<Long,Integer>> userIdAndIncrements);

    @Update("update tb_cmty_user set topic_count=topic_count+#{increment} where id=#{userId}")
    Integer updateTopicCountByIncrement(@Param("userId") long userId, @Param("increment") int increment);

    @Update("update tb_cmty_user set star_count=star_count+#{increment} where id=#{userId}")
    Integer updateStarCountByIncrement(@Param("userId") long userId, @Param("increment") int increment);

    @Select("select * from tb_cmty_user order by ${column} desc limit #{startIndex},#{recordNum}")
    List<CmtyUser> selectListOrderByColumn(@Param("column") String underlineColumnName,
                                                         @Param("startIndex") int startIndex,
                                                         @Param("recordNum") Integer recordNum);
}

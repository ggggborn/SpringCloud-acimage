package com.acimage.community.dao;

import com.acimage.common.model.domain.User;
import com.acimage.common.model.domain.UserBasic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserBasicDao extends BaseMapper<UserBasic> {

    @Select("select * from tb_user_basic where id=#{id}")
    User selectUserById(@Param("id") long id);
}

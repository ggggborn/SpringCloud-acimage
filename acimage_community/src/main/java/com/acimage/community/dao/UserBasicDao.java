package com.acimage.community.dao;

import com.acimage.common.model.domain.user.User;
import com.acimage.common.model.domain.community.UserBasic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserBasicDao extends BaseMapper<UserBasic> {

    @Select("select * from tb_user_basic where id=#{id}")
    User selectUserById(@Param("id") long id);
}

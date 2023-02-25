package com.acimage.user.dao;

import com.acimage.common.model.domain.user.UserMsg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface UserMsgDao extends BaseMapper<UserMsg> {

    @Update("update tb_user_msg set ${column}=${column}+#{increment} where user_id=#{userId}")
    void increaseColumn(@Param("userId") long userId,@Param("column") String column,@Param("increment") int increment);
}

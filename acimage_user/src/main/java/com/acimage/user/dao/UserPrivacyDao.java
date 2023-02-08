package com.acimage.user.dao;




import com.acimage.common.model.domain.user.UserPrivacy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface UserPrivacyDao extends BaseMapper<UserPrivacy> {

//    @Insert("insert into tb_user(id,username,pwd,email,salt) values (#{id},#{username},#{password},#{email},#{salt})")
//    void insert(@Param("id") long id,@Param("username") String username,@Param("password") String passwordDigest,
//                @Param("email") String email,@Param("salt") String salt);

//    @Update("update tb_user set username=#{username} where id=#{id}")
//    Integer updateUsername(@Param("id") long id, @Param("username") String username);


}

package com.acimage.user.dao;


import com.acimage.common.model.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface UserDao extends BaseMapper<User> {

//    @Insert("insert into tb_user(id,username,pwd,email,salt) values (#{id},#{username},#{password},#{email},#{salt})")
//    void insert(@Param("id") long id,@Param("username") String username,@Param("password") String passwordDigest,
//                @Param("email") String email,@Param("salt") String salt);

//    UserPrivacy selectUserInfoWithTopicCountStarCountById(@Param("id") long id);


//    @Insert("insert into tb_user(id,username,pwd,email,salt) values (#{id},#{username},#{password},#{email},#{salt})")
//    void insert(@Param("id") long id,@Param("username") String username,@Param("password") String passwordDigest,
//                @Param("email") String email,@Param("salt") String salt);
//
//    @Update("update tb_user set username=#{username} where id=#{id}")
//    Integer updateUsername(@Param("id") long id, @Param("username") String username);
//
//
//    @Select("select * from tb_user where username=#{username}")
//    UserPrivacy selectUserInfoByUsername(@Param("username") String username);
//
//    @Select("select * from tb_user where email=#{email}")
//    UserPrivacy selectUserInfoByEmail(@Param("email") String email);
//
//    UserPrivacy selectUserInfoWithTopicCountStarCountById(@Param("id") long id);
}

package com.example.mapper;

import com.example.entity.AuthAccount;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface AuthAccountMapper {
    @Select("SELECT id, username, password FROM t_account WHERE username = #{username} AND password = #{password}")
    AuthAccount findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    @Select("SELECT id, username, password FROM t_account WHERE username = #{username}")
    AuthAccount findByUsername(@Param("username") String username);

    @Select("SELECT COUNT(*) FROM t_account")
    int countAll();

    @Update("UPDATE t_account SET password = #{password} WHERE username = #{username}")
    int updatePassword(@Param("username") String username, @Param("password") String password);
}

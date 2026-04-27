package com.example.mapper;

import com.example.entity.User;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserMapper {
    @Select("SELECT * FROM t_user")
    List<User> findAll();

    @Select("SELECT * FROM t_user ORDER BY id LIMIT #{offset}, #{pageSize}")
    List<User> findPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    @Select("SELECT COUNT(*) FROM t_user")
    int countAll();

    @Select("SELECT * FROM t_user WHERE id = #{id}")
    User findById(Integer id);

    @Insert("INSERT INTO t_user(name, age, email) VALUES(#{name}, #{age}, #{email})")
    int add(User user);

    @Insert("INSERT INTO t_user(id, name, age, email) VALUES(#{id}, #{name}, #{age}, #{email})")
    int addWithId(User user);

    @Update("UPDATE t_user SET name=#{name}, age=#{age}, email=#{email} WHERE id=#{id}")
    int update(User user);

    @Delete("DELETE FROM t_user WHERE id=#{id}")
    int deleteById(Integer id);

    @Delete("DELETE FROM t_user")
    int deleteAll();

    @Select("SELECT * FROM t_user WHERE name LIKE CONCAT('%',#{name},'%')")
    List<User> findByName(String name);

    @Select("SELECT * FROM t_user WHERE name LIKE CONCAT('%',#{name},'%') ORDER BY id LIMIT #{offset}, #{pageSize}")
    List<User> findByNamePage(@Param("name") String name, @Param("offset") int offset, @Param("pageSize") int pageSize);

    @Select("SELECT COUNT(*) FROM t_user WHERE name LIKE CONCAT('%',#{name},'%')")
    int countByName(String name);
}

package com.example.mapper;

import com.example.entity.OperationLog;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface OperationLogMapper {
    @Insert("INSERT INTO t_operation_log(username, action, detail) VALUES(#{username}, #{action}, #{detail})")
    int add(OperationLog log);

    @Select("SELECT id, username, action, detail, created_at AS createdAt FROM t_operation_log ORDER BY id DESC LIMIT #{offset}, #{pageSize}")
    List<OperationLog> findPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    @Select("SELECT COUNT(*) FROM t_operation_log")
    int countAll();
}

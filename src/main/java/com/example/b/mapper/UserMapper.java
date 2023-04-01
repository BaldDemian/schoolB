package com.example.b.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.b.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from \"USER\" where 账户名 = #{name}")
    User findByName(@Param("name") String name);

    @Insert("insert into \"USER\" (账户名, 密码, 级别, 客体) values(#{name}, #{password}, #{auth}, #{student})")
    void insertOne(User user);

}

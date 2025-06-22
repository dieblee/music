package com.xm.mapper;

import com.xm.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


import java.util.List;

@Mapper
public interface EmmMapper {
    List<User> selectAll(User user);
    @Select("select * from music.admin where id = #{id}")
    User selectById(Integer id);

    void insert(User user);

    void updateById(User user);
    @Delete("delete from music.admin where id = #{id}")
    void deleteById(Integer id);
    @Select("select * from music.admin where name = #{name}")
    User selectByName(String name);
}

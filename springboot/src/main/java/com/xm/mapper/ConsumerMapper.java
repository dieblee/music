package com.xm.mapper;

import com.xm.entity.Consumer;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

public interface ConsumerMapper {
    List<Consumer> selectAll(Consumer consumer);
    @Select("select * from music.consumer where id = #{id}")
    Consumer selectById(Integer id);
    @Select("select * from music.consumer where username = #{username}")
    Consumer selectByUsername(String username);
    void insert(Consumer consumer);

    void updateTime(Consumer consumer);
    void updateById(Consumer consumer);
    @Delete("delete from music.consumer where id = #{id}")
    void deleteById(Integer id);

}

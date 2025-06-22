package com.xm.mapper;
import com.xm.entity.Singer;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SingerMapper {
    List<Singer> selectAll(Singer singer);
    @Select("select * from music.singer where id = #{id}")
    Singer selectById(Integer id);
    List<Singer> groupByType();

    void insert(Singer singer);
    void updateName(String oldname,String newname);

    void updateById(Singer singer);
    @Delete("delete from music.singer where id = #{id}")
    void deleteById(Integer id);
    @Select("SELECT sg.*, s.name AS songName, s.pic AS songPic, s.url AS songUrl " +
            "FROM music.singer sg " +
            "JOIN music.song s ON sg.id = s.singer_id " +
            "WHERE sg.name = #{name} " +
            "LIMIT #{pageSize} OFFSET #{offset}")
    List<Singer> selectBySongsname(String name,int pageSize,int offset);
    @Select("SELECT COUNT(*) FROM music.singer sg " +
            "JOIN music.song s ON sg.id = s.singer_id " +
            "WHERE sg.name = #{name}")
    Integer countBySongsname(String name);
    @Select("SELECT * from music.singer where name = #{name}")
    Singer selectByName(String name);
    @Select("SELECT type from music.singer where id= #{id}")
    String selectType(Integer id);

}

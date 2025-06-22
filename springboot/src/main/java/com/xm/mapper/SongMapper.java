package com.xm.mapper;

import com.xm.entity.Singer;
import com.xm.entity.Song;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SongMapper {
    List<Song> selectAll(Song song);
    @Select("select * from music.song where id = #{id}")
    Song selectById(Integer id);

    void insert(Song song);

    void updateById(Song song);
    @Delete("delete from music.song where id = #{id}")
    void deleteById(Integer id);
    @Select("select * from music.song where name = #{name}")
    List<Song> selectByName(String name);
    @Select("SELECT s.*, sg.name AS singerName " +
            "FROM music.song s " +
            "JOIN music.singer sg ON s.singer_id = sg.id " +
            "WHERE s.address = #{address}")
    List<Song> selectByAddress(String address);


    List<Song> selectByNameAndSingerId(String name, Integer singerId);
}

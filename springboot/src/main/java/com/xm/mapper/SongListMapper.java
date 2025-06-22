package com.xm.mapper;
import com.xm.entity.Singer;
import com.xm.entity.SongList;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SongListMapper {
    List<SongList> selectAll(SongList songList);

    List<SongList> selectSongList (SongList songList);
    @Select("select * from music.song_list where id = #{id}")
    SongList selectById(Integer id);


    void insert(SongList songList);

    void updateById(SongList songList);
    @Delete("delete from music.song_list where id = #{id}")
    void deleteById(Integer id);
    @Select("SELECT sl.*, cs.username AS username " +
            "FROM music.song_list sl " +
            "JOIN music.consumer cs ON sl.create_id = cs.id " +
            "WHERE sl.title = #{name}")
    SongList selectByName(String name);
    @Select("select * from music.song_list where create_id = #{id}")
    List<SongList> selectByCreateId(Integer id);

}

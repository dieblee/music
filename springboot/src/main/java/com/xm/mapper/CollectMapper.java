package com.xm.mapper;

import com.xm.entity.Collect;

import com.xm.entity.Song;
import com.xm.entity.SongList;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CollectMapper {
    List<Collect> selectAll(Collect collect);
    @Select("select * from music.collect where id = #{id}")
    Collect selectById(Integer id);
    @Select("select * from music.collect where song_id=#{useId} and user_id=#{userId}")
    Collect selectCollectSong(Integer useId,Integer userId);

    @Select("SELECT sl.* FROM music.collect c " +
            "JOIN music.song_list sl ON c.song_list_id = sl.id " +
            "WHERE c.user_id = #{id}")
    List<SongList> selectSongListByUserId(Integer id);

    @Select("SELECT s.*,sg.name As singerName FROM music.collect c " +
            "JOIN music.song s ON c.song_id = s.id " +
            "JOIN music.singer sg ON s.singer_id = sg.id " +
            "WHERE c.user_id = #{id}")
    List<Song> selectSongByUserId(Integer id);
    @Select("select * from music.collect where song_list_id=#{songListId} and user_id=#{userId}")
    Collect selectCollectSongList(Integer songListId,Integer userId);

    void insert(Collect collect);

    void updateById(Collect swiper);
    @Delete("delete from music.collect where id = #{id}")
    void deleteById(Integer id);
    @Delete("delete from music.collect where user_id = #{userId} and song_id=#{useId}")
    void removeBySongId(Integer useId,Integer userId);

    @Delete("delete from music.collect where user_id = #{userId} and song_list_id=#{useId}")
    void removeBySongListId(Integer useId,Integer userId);
}

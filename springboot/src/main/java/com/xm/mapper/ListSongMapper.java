package com.xm.mapper;

import com.xm.entity.ListSong;
import com.xm.entity.Song;
import com.xm.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ListSongMapper {
    List<ListSong> selectAll(ListSong listSong);
    @Select("select * from music.list_song where id = #{id}")
    ListSong selectById(Integer id);
    @Select("SELECT s.* FROM music.list_song ls " +
            "JOIN music.song s ON ls.song_id = s.id " +
            "WHERE ls.song_list_id = #{id}")
    List<Song> selectAllSongs(Integer id);
    @Select("SELECT COUNT(*) > 0 FROM list_song WHERE song_list_id = #{songListId} AND song_id = #{songId}")
    boolean existsBySongListIdAndSongId(Integer songListId,Integer songId);
    void insert(ListSong listSong);

    void updateById(ListSong listSong);
    @Delete("delete from music.list_song where id = #{id}")
    void deleteById(Integer id);
    @Delete("delete from music.list_song where  song_list_id = #{songListId} AND song_id = #{songId}")
    void manageSongs(Integer songListId,Integer songId);
}

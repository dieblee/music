package com.xm.mapper;

import com.xm.entity.Statistic;
import com.xm.entity.Swiper;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

public interface StatisticMapper {
    List<Statistic> selectAll(Statistic statistic);
    @Select("select * from music.swiper where id = #{id}")
    Statistic selectById(Integer id);

    @Select("SELECT click FROM statistic WHERE song_id = #{songId}")
    Integer getClickCount(Integer songId);

    @Select("SELECT date FROM statistic WHERE song_id = #{songId}")
    Date getClickDate(Integer songId);

    @Select("SELECT date FROM statistic WHERE song_list_id = #{songListId}")
    Date getslClickDate(Integer songListId);

    @Update("UPDATE statistic SET click = click + 1, date = NOW() WHERE song_id = #{songId}")
    void updateClickCount(Integer songId);
    @Update("UPDATE statistic SET click = click + 1, date = NOW(),oldclick=click WHERE song_id = #{songId}")
    void updateOldClickCount(Integer songId);
    @Update("UPDATE statistic SET click = click + 1, date = NOW(),oldclick=click WHERE  song_list_id = #{songListId}")
    void updateslOldClickCount(Integer songListId);
    @Update("UPDATE statistic SET click = click + 1, date = NOW() WHERE song_list_id = #{songId}")
    void updateslClickCount(Integer songId);

    void insert(Statistic statistic);

    void updateById(Statistic statistic);
    @Delete("delete from music.statistic where id = #{id}")
    void deleteById(Integer id);

    @Select("SELECT SUM(st.click) AS SongClicks " +
            "FROM music.statistic st " +
            "WHERE st.song_id IS NOT NULL")
    Integer countSongsclick();

    @Select("SELECT SUM(st.click)-SUM(st.oldclick) AS newClicks " +
            "FROM music.statistic st " +
            "WHERE st.date = #{date} ")
    Integer countNewClick(@Param("date") String date);


    @Select("SELECT SUM(st.click) AS totalClicks, s.singer_id, sg.name AS singerName " +
            "FROM statistic st " +
            "JOIN song s ON st.song_id = s.id " +
            "JOIN singer sg ON s.singer_id = sg.id " +
            "WHERE st.song_id IS NOT NULL " +
            "GROUP BY s.singer_id, sg.name " +  // 按歌手ID和歌手名称分组
            "ORDER BY totalClicks DESC")  // 可选，按点击量降序排序
    @Results({
            @Result(property = "singerName", column = "singerName"),
            @Result(property = "singerId", column = "singer_id"),
            @Result(property = "totalClicks", column = "totalClicks")
    })
    List<Statistic> rankBysingerclick();
    @Select("SELECT  st.*,s.pic AS songPic,s.name AS songTitle,s.url AS songUrl, sg.name AS singerName " +
            "FROM statistic st " +
            "JOIN song s ON st.song_id = s.id " +
            "JOIN singer sg ON s.singer_id = sg.id " +
            "WHERE st.song_id IS NOT NULL " +
            "ORDER BY st.click DESC"
    )
    @Result(property = "songPic", column = "songPic")
    @Result(property = "songTitle", column = "songTitle")
    @Result(property = "songUrl", column = "songUrl")
    List<Statistic> rankBysongclick();
    @Select("SELECT  st.*,cs.username AS createName, sl.title AS songListTitle,sl.style AS songListStyle,sl.pic AS songListPic " +
            "FROM statistic st " +
            "JOIN song_list sl ON st.song_list_id = sl.id " +
            "JOIN consumer cs ON sl.create_id = cs.id " +
            "WHERE st.song_list_id IS NOT NULL " +
            "ORDER BY st.click DESC"
    )
    @Result(property = "createName", column = "createName")
    @Result(property = "songListStyle", column = "songListStyle")
    @Result(property = "songListTitle", column = "songListTitle")
    List<Statistic> rankBysonglistclick();
}

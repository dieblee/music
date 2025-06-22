package com.xm.mapper;

import com.xm.entity.Comment;
import com.xm.entity.Singer;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CommentMapper {
    List<Comment> selectAll(Comment comment);
    @Select("select * from music.comment where id = #{id}")
    Comment selectById(Integer id);
    @Select("SELECT c.*, cs.username AS username,cs.avator AS avator " +
            "FROM music.comment c " +
            "JOIN music.consumer cs ON c.user_id = cs.id " +
            "WHERE c.song_list_id = #{song_list_id}")
    List<Comment> selectBYSongListId(Integer song_list_id);
    @Select("SELECT c.*, cs.username AS username,cs.avator AS avator " +
            "FROM music.comment c " +
            "JOIN music.consumer cs ON c.user_id = cs.id " +
            "WHERE c.song_id = #{song_id}")
    List<Comment> selectBYSongId(Integer song_id);
    void insert(Comment comment);

    void updateById(Comment comment);
    @Delete("delete from music.comment where id = #{id}")
    void deleteById(Integer id);
}

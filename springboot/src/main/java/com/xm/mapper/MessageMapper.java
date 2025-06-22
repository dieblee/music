package com.xm.mapper;

import com.xm.entity.Message;
import com.xm.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


import java.util.List;

@Mapper
public interface MessageMapper {
    List<Message> selectAll(Message message);
    @Select("SELECT m.*, cs.username AS receiveName " +
            "FROM music.message m " +
            "JOIN music.consumer cs ON m.receive_id = cs.id " +
            "WHERE m.receive_id = #{id}")
    List<Message> selectByReceiveId(Integer id);
    @Select("SELECT m.*, cs.username AS receiveName " +
            "FROM music.message m " +
            "JOIN music.consumer cs ON m.receive_id = cs.id " +
            "WHERE m.send_id = #{id}")
    List<Message> selectBySendId(Integer id);

    void insert(Message message);

    void updateById(Message message);

    @Delete("delete from music.message where id = #{id}")
    void deleteById(Integer id);

}
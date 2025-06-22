package com.xm.mapper;


import com.xm.entity.Statue;
import org.apache.ibatis.annotations.*;


import java.util.List;

@Mapper
public interface StatueMapper {
    List<Statue> selectAll(Statue message);
    @Select("SELECT DISTINCT s.*, cs.username AS receiveName " +
            "FROM music.statue s " +
            "JOIN music.consumer cs ON s.receive_id = cs.id " +
            "WHERE s.statue= 'open'and (s.send_id = #{id} OR s.receive_id = #{id})")
    @Result(property = "receiveName", column = "receiveName")  // 确保 receiveName 被映射
    List<Statue> selectByStatue(Integer id);

    void insert(Statue message);

    void updateById(Statue message);

    @Update("UPDATE music.statue SET statue = 'closed' WHERE receive_id = #{id}")
    void updateStatue(Integer id);

    @Update("UPDATE music.statue SET statue = 'open' WHERE receive_id = #{id}")
    void changeStatue(Integer id);

    @Delete("delete from music.admin where id = #{id}")
    void deleteById(Integer id);

}
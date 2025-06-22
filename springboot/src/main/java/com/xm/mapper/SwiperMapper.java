package com.xm.mapper;

import com.xm.entity.Swiper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SwiperMapper {
    List<Swiper> selectAll(Swiper swiper);
    @Select("select * from music.swiper where id = #{id}")
    Swiper selectById(Integer id);

    void insert(Swiper swiper);

    void updateById(Swiper swiper);
    @Delete("delete from music.swiper where id = #{id}")
    void deleteById(Integer id);
}

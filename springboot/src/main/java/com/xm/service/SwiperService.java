package com.xm.service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xm.entity.Account;
import com.xm.entity.Singer;
import com.xm.entity.Song;
import com.xm.entity.Swiper;
import com.xm.exception.CustomException;
import com.xm.mapper.SingerMapper;
import com.xm.mapper.SongMapper;
import com.xm.mapper.SwiperMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service//标志为spingboot中的容器
public class SwiperService {
    @Resource
    private SwiperMapper swiperMapper;

    public List<Swiper> selectAll(Swiper swiper) {
        List<Swiper> list = swiperMapper.selectAll(swiper);
        return list;
    }

    public Swiper selectById(Integer id) {return swiperMapper.selectById(id);
    }
    public void update(Swiper swiper) {
        swiperMapper.updateById(swiper);
    }

    public void deleteById(Integer id) {
        swiperMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }
    public void add(Swiper swiper) {
        swiperMapper.insert(swiper);
    }

}
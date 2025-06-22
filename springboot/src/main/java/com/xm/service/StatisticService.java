package com.xm.service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xm.entity.*;
import com.xm.exception.CustomException;
import com.xm.mapper.SingerMapper;
import com.xm.mapper.SongMapper;
import com.xm.mapper.StatisticMapper;
import com.xm.mapper.SwiperMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service//标志为spingboot中的容器
public class StatisticService {
    @Resource
    private StatisticMapper statisticMapper;

    public List<Statistic> selectAll(Statistic statistic) {
        List<Statistic> list = statisticMapper.selectAll(statistic);
        return list;
    }

    public Statistic selectById(Integer id) {return statisticMapper.selectById(id);
    }
    public void update(Statistic statistic) {
        statisticMapper.updateById(statistic);
    }

    public void deleteById(Integer id) {
        statisticMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }
    public void add(Statistic statistic) {
        statisticMapper.insert(statistic);
    }

    public Integer getClickCount(Integer songId) {
        return statisticMapper.getClickCount(songId);
    }
    public Date getClickDate(Integer songId) {
        return statisticMapper.getClickDate(songId);
}
    public Date getslClickDate(Integer songListId) {
        return statisticMapper.getslClickDate(songListId);
    }
    public Integer getTodayNewClicks(String date) {
        return statisticMapper.countNewClick(date);
    }
    public Integer countSongsclick() {
        return statisticMapper.countSongsclick();
    }

    public List<Statistic> rankBysingerclick() {
        return statisticMapper.rankBysingerclick();
    }
    public List<Statistic> rankBysonglistclick() {
        return statisticMapper.rankBysonglistclick();
    }
    public List<Statistic> rankBysongclick() {
        return statisticMapper.rankBysongclick();
    }


    // 更新点击次数
    public void updateClickCount(Integer songId) {
        statisticMapper.updateClickCount(songId);
    }
    public void updateOldClickCount(Integer songId) {
        statisticMapper.updateOldClickCount(songId);
    }
    public void updateslOldClickCount(Integer songListId) {
        statisticMapper.updateslOldClickCount(songListId);
    }
    public void updateslClickCount(Integer songListId) {
        statisticMapper.updateslClickCount(songListId);
    }

}
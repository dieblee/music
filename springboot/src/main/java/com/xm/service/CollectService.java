package com.xm.service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xm.entity.*;
import com.xm.exception.CustomException;
import com.xm.mapper.CollectMapper;
import com.xm.mapper.SingerMapper;
import com.xm.mapper.SongMapper;
import com.xm.mapper.SwiperMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service//标志为spingboot中的容器
public class CollectService {
    @Resource
    private CollectMapper collectMapper;

    public List<Collect> selectAll(Collect collect) {
        List<Collect> list = collectMapper.selectAll(collect);
        return list;
    }

    public Collect selectById(Integer id) {return collectMapper.selectById(id);
    }
    public List<SongList> selectSongListByUserId(Integer id) {return collectMapper.selectSongListByUserId(id);
    }

    public List<Song> selectSongByUserId(Integer id) {return collectMapper.selectSongByUserId(id);
    }
    public Collect selectCollectSong(Integer useId,Integer userId){return collectMapper.selectCollectSong(useId,userId);}
    public Collect selectCollectSongList(Integer songListId,Integer userId){return collectMapper.selectCollectSongList(songListId,userId);}
    public void update(Collect collect) {
        collectMapper.updateById(collect);
    }

    public void deleteById(Integer id) {
        collectMapper.deleteById(id);
    }

    public void removeBySongId(Integer useId,Integer userId) {
        collectMapper.removeBySongId(useId,userId);
    }
    public void removeBySongListId(Integer useId,Integer userId) {
        collectMapper.removeBySongListId(useId,userId);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }
    public void add(Collect collect) {
        collectMapper.insert(collect);
    }

}
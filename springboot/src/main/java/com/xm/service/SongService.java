package com.xm.service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xm.entity.*;
import com.xm.exception.CustomException;
import com.xm.mapper.SingerMapper;
import com.xm.mapper.SongMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service//标志为spingboot中的容器
public class SongService {
    @Resource
    private SongMapper songMapper;

    public List<Song> selectAll(Song song) {
        List<Song> list = songMapper.selectAll(song);
        return list;
    }

    public Song selectById(Integer id) {return songMapper.selectById(id);
    }
    public List<Song> selectByName(String name) {return songMapper.selectByName(name);
    }
    public List<Song> selectByAddress(String address) {return songMapper.selectByAddress(address);
    }
    public PageInfo<Song> selectPage(Song song, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Song> list = songMapper.selectAll(song);
        return PageInfo.of(list);
    }
    public void update(Song song) {
        songMapper.updateById(song);
    }

    public void deleteById(Integer id) {
        songMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }
    public void add(Song song) {
        List<Song> existingSongs = songMapper.selectByNameAndSingerId(song.getName(), song.getSingerId());
        if (!existingSongs.isEmpty()) {
            throw new CustomException("500", "歌曲重复");
        }
        songMapper.insert(song);
    }


}
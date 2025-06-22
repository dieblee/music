package com.xm.service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xm.entity.SongList;
import com.xm.mapper.SongListMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service//标志为spingboot中的容器
public class SongListService {
    @Resource
    private SongListMapper songListMapper;

    public List<SongList> selectAll(SongList songList) {
        List<SongList> list = songListMapper.selectAll(songList);
        return list;
    }
    public List<SongList> selectSongList(SongList songList) {
        List<SongList> list = songListMapper.selectSongList(songList);
        return list;
    }

    public SongList selectById(Integer id) {return songListMapper.selectById(id);
    }
    public SongList selectByName(String name) {return songListMapper.selectByName(name);
    }
    public List<SongList> selectByCreateId(Integer id) {return songListMapper.selectByCreateId(id);
    }


    public PageInfo<SongList> selectPage(SongList songList, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SongList> list = songListMapper.selectSongList(songList);
        return PageInfo.of(list);
    }
    public void update(SongList songList) {
        songListMapper.updateById(songList);
    }

    public void deleteById(Integer id) {
        songListMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }
    public void add(SongList song) {
        songListMapper.insert(song);
    }

}
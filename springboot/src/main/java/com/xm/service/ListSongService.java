package com.xm.service;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xm.entity.Consumer;
import com.xm.entity.ListSong;
import com.xm.entity.Song;
import com.xm.exception.CustomException;
import com.xm.mapper.ConsumerMapper;
import com.xm.mapper.ListSongMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service//标志为spingboot中的容器
public class ListSongService {
    @Resource
    private ListSongMapper lsMapper;

    public List<ListSong> selectAll(ListSong ls) {
        List<ListSong> list = lsMapper.selectAll(ls);
        return list;
    }

    public List<Song> selectAllSongs(Integer id){return  lsMapper.selectAllSongs(id); }
    public ListSong selectById(Integer id) {
        return lsMapper.selectById(id);
    }

    public void add(ListSong ls) {
        Integer songListId = ls.getSong_list_id();
        Integer songId = ls.getSong_id();

        if (lsMapper.existsBySongListIdAndSongId(songListId, songId)) {
            throw new CustomException("500", "歌曲已存在");
        }

        lsMapper.insert(ls);
    }
    public void update(ListSong ls) {
        lsMapper.updateById(ls);
    }

    public void deleteById(Integer id) {
        lsMapper.deleteById(id);
    }
    public void manageSongs(Integer songListId,Integer songId) {lsMapper.manageSongs(songListId,songId);
    }
}



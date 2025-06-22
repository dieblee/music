package com.xm.service;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xm.entity.Comment;
import com.xm.entity.Consumer;
import com.xm.entity.ListSong;
import com.xm.exception.CustomException;
import com.xm.mapper.CommentMapper;
import com.xm.mapper.ConsumerMapper;
import com.xm.mapper.ListSongMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service//标志为spingboot中的容器
public class CommentService {
    @Resource
    private CommentMapper commentMapper;

    public List<Comment> selectAll(Comment comment) {
        List<Comment> list = commentMapper.selectAll(comment);
        return list;
    }

    public Comment selectById(Integer id) {
        return commentMapper.selectById(id);
    }
    public List<Comment> selectBySongListId(Integer song_list_id){return commentMapper.selectBYSongListId(song_list_id);}
    public List<Comment> selectBySongId(Integer song_id){
        return commentMapper.selectBYSongId(song_id);
    }

    public void add(Comment comment) {
        commentMapper.insert(comment);
    }
    public void update(Comment comment) {
        commentMapper.updateById(comment);
    }

    public void deleteById(Integer id) {
        commentMapper.deleteById(id);
    }

    public PageInfo<Comment> selectPage(Comment comment, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> list = commentMapper.selectAll(comment);
        return PageInfo.of(list);
    }
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }


}



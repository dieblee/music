package com.xm.service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xm.entity.Account;
import com.xm.entity.Singer;
import com.xm.exception.CustomException;
import com.xm.mapper.SingerMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service//标志为spingboot中的容器
public class SingerService {
    @Resource
    private SingerMapper singerMapper;

    public List<Singer> selectAll(Singer singer) {
        List<Singer> list = singerMapper.selectAll(singer);
        return list;
    }

    public Singer selectById(Integer id) {return singerMapper.selectById(id);
    }
    public Singer selectByName(String name) {return singerMapper.selectByName(name);
    }
    public List<Singer> selectBySongsname(String name,int pageSize,int offset) {return singerMapper.selectBySongsname(name,pageSize,offset);
    }
    public List<Singer> getSingerCountByType() {return singerMapper.groupByType();}
    public Integer countBySongsname(String name){return singerMapper.countBySongsname(name);};

    public PageInfo<Singer> selectPage(Singer singer, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Singer> list = singerMapper.selectAll(singer);
        return PageInfo.of(list);
    }
    public void update(Singer singer) {
        singerMapper.updateById(singer);
    }
    public void updateName(String oldname, String newname) {
        singerMapper.updateName(oldname, newname);
    }

    public void deleteById(Integer id) {
        singerMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }
    public void add(Singer singer) {
        String name = singer.getName();
        Singer dbsinger = singerMapper.selectByName(name);
        if(dbsinger  != null){
            throw  new CustomException("500","账号已存在，请更换账号");
        }
        singerMapper.insert(singer);
    }

    public void modify(Account account) {
    }

    public String selectType(Integer id) {return singerMapper.selectType(id);}


}
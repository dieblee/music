package com.xm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xm.entity.Statue;
import com.xm.mapper.StatueMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
@Service//标志为spingboot中的容器
public class StatueService {
    @Resource
    private StatueMapper statueMapper;

    public List<Statue> selectAll(Statue statue) {
        List<Statue> list = statueMapper.selectAll(statue);
        return list;
    }

    public void update(Statue statue) {
        statueMapper.updateById(statue);
    }
    public void updateStatue(Integer id) {statueMapper.updateStatue(id);}
    public void changeStatue(Integer id) {statueMapper.changeStatue(id);}
    public void deleteById(Integer id) {
        statueMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }
    public  void changeBatch(List<Integer> ids){
        for (Integer id : ids) {
            this.changeStatue(id);
        }
    }
    public void add(Statue statue) {
        statueMapper.insert(statue);
    }
    public List<Statue> selectByStatue(Integer id) {return statueMapper. selectByStatue(id);
    }
}

package com.xm.service;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xm.entity.Consumer;
import com.xm.entity.Song;
import com.xm.entity.User;
import com.xm.exception.CustomException;
import com.xm.mapper.ConsumerMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service//标志为spingboot中的容器
public class ConsumerService {
    @Resource
    private ConsumerMapper consumerMapper;

    public List<Consumer> selectAll(Consumer consumer) {
        List<Consumer> list = consumerMapper.selectAll(consumer);
        return list;
    }

    public Consumer selectById(Integer id) {
        return consumerMapper.selectById(id);
    }

    public Consumer selectByUsername(String username) {
        return consumerMapper.selectByUsername(username);
    }

    public PageInfo<Consumer> selectPage(Consumer consumer, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Consumer> list = consumerMapper.selectAll(consumer);
        return PageInfo.of(list);
    }

    public void add(Consumer consumer) {
        String username = consumer.getUsername();
        if (StrUtil.isBlank(username)) {
            throw new CustomException("500", "用户名不能为空");
        }
        Consumer dbconsumer = consumerMapper.selectByUsername(username);
        if (dbconsumer != null) {
            throw new CustomException("500", "账号已存在，请更换账号");
        }
        if (StrUtil.isBlank(consumer.getPassword())) {
            throw new CustomException("500", "密码不能为空");
        }
        consumerMapper.insert(consumer);
    }
    public void updateTime(Consumer consumer){ consumerMapper.updateTime(consumer);}

    public void update(Consumer consumer) {
        consumerMapper.updateById(consumer);
    }

    public void deleteById(Integer id) {
        consumerMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }
    public Consumer login(Consumer consumer ) {
        String name = consumer.getUsername();
        Consumer dbconsumer = consumerMapper.selectByUsername(name);
        if(dbconsumer  == null){
            throw  new CustomException("500","用户不存在");
        }
        String password = dbconsumer.getPassword();
        if(!dbconsumer.getPassword().equals(password)){
            throw  new CustomException("500","密码错误");
        }
        return dbconsumer;
    }
}


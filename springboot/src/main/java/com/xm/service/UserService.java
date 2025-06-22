package com.xm.service;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xm.entity.Account;
import com.xm.entity.User;
import com.xm.exception.CustomException;
import com.xm.mapper.EmmMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service//标志为spingboot中的容器
//链接controller和外部
public class UserService {
    @Resource
    private EmmMapper emmMapper;
    public List<User> selectAll(User user){
        List<User> list = emmMapper.selectAll(user);
        return  list;
    }

    public User selectById(Integer id) {
        return emmMapper.selectById(id);
    }
    public PageInfo<User> selectPage(User user,Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<User> list = emmMapper.selectAll(user);
        return PageInfo.of(list);
    }

    public void add(User user) {
        String name = user.getName();
        User dbuser = emmMapper.selectByName(name);
        if(dbuser  != null){
            throw  new CustomException("500","账号已存在，请更换账号");
        }
        if(StrUtil.isBlank(user.getPassword())){
            user.setPassword("123456");
        }
        if(StrUtil.isBlank(user.getName())){
            user.setName("dieble");
        }
        user.setRole("EMP");
        user.setAvator("https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png");
        emmMapper.insert(user);
    }
    public void update(User user) {
        emmMapper.updateById(user);
    }

    public void deleteById(Integer id) {
        emmMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for(Integer id:ids){
            this.deleteById(id);
        }
    }

    public User login(User user ) {
        String name = user.getName();
        User dbuser = emmMapper.selectByName(name);
        if(dbuser  == null){
            throw  new CustomException("500","用户不存在");
        }
        String password = user.getPassword();
        if(!dbuser.getPassword().equals(password)){
            throw  new CustomException("500","密码错误");
        }
        String role = user.getRole();
        if(!dbuser.getRole().equals(role)){
            throw  new CustomException("500","状态错误");
        }
        return dbuser;
    }

    public void register(User user) {
        String name = user.getName();
        User dbuser = emmMapper.selectByName(name);
        if(dbuser  != null){
            throw  new CustomException("500","账号已存在，请更换账号");
        }
        this.add(user);
    }

    public void modify(Account account){
        Integer id = account.getId();
        User dbuser = this.selectById(id);
        if(dbuser.getPassword().equals(account.getNewPassword())){
            throw  new CustomException("500","新旧密码一致，请更换密码");
        }
        else if(dbuser.getPassword().equals(account.getPassword())){
            throw  new CustomException("500","原密码不对，请重新输入");
        }
        dbuser.setPassword(account.getNewPassword());
        this.update(dbuser);
    }
}

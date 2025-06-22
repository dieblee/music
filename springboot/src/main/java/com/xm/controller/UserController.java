package com.xm.controller;

import com.github.pagehelper.PageInfo;
import com.xm.common.Result;
import com.xm.entity.Account;
import com.xm.entity.User;
import com.xm.exception.CustomException;
import com.xm.service.UserService;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    /*新增数据*/
    @PostMapping("/add")
    public Result add(@RequestBody User user){
        userService.add(user);
        return Result.success();
    }
    /*更新数据*/
    @PutMapping("/update")
    public Result update(@RequestBody User user){
        userService.update(user);
        return Result.success();
    }
    /*删除数据*/
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id){
        userService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){//接受数组集合
        userService.deleteBatch(ids);
        return Result.success();
    }
    @GetMapping("/selectAll")
    public Result selectAll(User user){
        List<User> list = userService.selectAll(user);
        return Result.success(list);
    }
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        User user = userService.selectById(id);
        return Result.success(user);
    }
    @GetMapping("/selectPage")
    public Result selectPage(
            User user,
            @RequestParam(defaultValue = "1")Integer pageNum,
            @RequestParam(defaultValue = "10")Integer pageSize){
        PageInfo<User> pageInfo = userService.selectPage(user,pageNum,pageSize);
        return Result.success(pageInfo);
    }
    @PutMapping("/modify")
    public Result modify(@RequestBody Account account){
        userService.modify(account);
        return Result.success();
    }
}

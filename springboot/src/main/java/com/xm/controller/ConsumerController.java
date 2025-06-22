package com.xm.controller;

import com.github.pagehelper.PageInfo;
import com.xm.common.Result;
import com.xm.entity.Consumer;
import com.xm.entity.User;
import com.xm.exception.CustomException;
import com.xm.service.ConsumerService;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {
    @Resource
    private ConsumerService consumerService;
    /*新增数据*/
    @PostMapping("/add")
    public Result add(@RequestBody Consumer consumer){
        consumerService.add(consumer);
        return Result.success();
    }
    /*更新数据*/
    @PutMapping("/update")
    public Result update(@RequestBody Consumer consumer){
        consumerService.update(consumer);
        return Result.success();
    }
    @PutMapping("/updatetime")
    public Result updateTime(@RequestBody Consumer consumer){
        consumerService.updateTime(consumer);
        return Result.success();
    }
    /*删除数据*/
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id){
        consumerService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){//接受数组集合
        consumerService.deleteBatch(ids);
        return Result.success();
    }
    @GetMapping("/selectAll")
    public Result selectAll(Consumer consumer){
        List<Consumer> list = consumerService.selectAll(consumer);
        return Result.success(list);
    }
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Consumer consumer = consumerService.selectById(id);
        return Result.success(consumer);
    }
    @GetMapping("/selectByUsername/{username}")
    public Result selectByUsername(@PathVariable String username){
        Consumer consumer = consumerService.selectByUsername(username);
        return Result.success(consumer);
    }
    @GetMapping("/selectPage")
    public Result selectPage(
            Consumer consumer,
            @RequestParam(defaultValue = "1")Integer pageNum,
            @RequestParam(defaultValue = "10")Integer pageSize){
        PageInfo<Consumer> pageInfo = consumerService.selectPage(consumer,pageNum,pageSize);
        return Result.success(pageInfo);
    }
    @PostMapping("/login")
    public Result login(@RequestBody Consumer consumer){
        Consumer dbconsumer = consumerService.login(consumer);
        return Result.success( dbconsumer);
    }
}

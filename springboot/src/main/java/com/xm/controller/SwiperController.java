package com.xm.controller;

import com.xm.common.Result;
import com.xm.entity.*;
import com.xm.service.SwiperService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/swiper")
public class SwiperController {
    @Resource
    private SwiperService swiperService;
    /*新增数据*/
    @PostMapping("/add")
    public Result add(@RequestBody Swiper swiper){
        swiperService.add(swiper);
        return Result.success();
    }
    /*更新数据*/
    @PutMapping("/update")
    public Result update(@RequestBody Swiper swiper){
        swiperService.update(swiper);
        return Result.success();
    }
    /*删除数据*/
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id){
        swiperService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){//接受数组集合
        swiperService.deleteBatch(ids);
        return Result.success();
    }
    @GetMapping("/selectAll")
    public Result selectAll(Swiper swiper){
        List<Swiper> list = swiperService.selectAll(swiper);
        return Result.success(list);
    }
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Swiper swiper = swiperService.selectById(id);
        return Result.success(swiper);
    }

}

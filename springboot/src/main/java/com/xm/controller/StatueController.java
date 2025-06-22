package com.xm.controller;

import com.github.pagehelper.PageInfo;
import com.xm.common.Result;
import com.xm.entity.Statue;
import com.xm.exception.CustomException;
import com.xm.service.ListSongService;
import com.xm.service.MessageService;
import com.xm.service.StatueService;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statue")
public class StatueController {
    @Resource
    private StatueService statueService;
    /*新增数据*/
    @PostMapping("/add")
    public Result add(@RequestBody Statue statue){
        statueService.add(statue);
        return Result.success();
    }
    /*更新数据*/
    @PutMapping("/update")
    public Result update(@RequestBody  Statue  statue){
        statueService.update(statue);
        return Result.success();
    }

    /*删除数据*/
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id){
        statueService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll( Statue statue){
        List< Statue> list = statueService.selectAll(statue);
        return Result.success(list);
    }
    @GetMapping("/selectByStatue/{id}")
    public Result selectByStatue(@PathVariable Integer id){
        List<Statue> list = statueService.selectByStatue(id);
        return Result.success(list);
    }
    @PutMapping("/updateStatue/{id}")
    public Result updateStatue(@PathVariable Integer id) {
        statueService.updateStatue(id);
        return Result.success();
    }
    @PutMapping("/changeStatue")
    public Result changeStatue(@RequestBody List<Integer> ids) {
        statueService.changeBatch(ids);
        return Result.success();
    }

}


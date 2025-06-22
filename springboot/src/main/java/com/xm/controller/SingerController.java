package com.xm.controller;

import com.github.pagehelper.PageInfo;
import com.xm.common.Result;
import com.xm.entity.Account;
import com.xm.entity.Singer;
import com.xm.entity.User;
import com.xm.service.SingerService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/singer")
public class SingerController {
    @Resource
    private SingerService singerService;
    /*新增数据*/
    @PostMapping("/add")
    public Result add(@RequestBody Singer singer){
        singerService.add(singer);
        return Result.success();
    }
    /*更新数据*/
    @PutMapping("/update")
    public Result update(@RequestBody Singer singer){
        singerService.update(singer);
        return Result.success();
    }
    @PutMapping("/updatename")
    public Result updatename(@RequestBody Map<String, String> names) {
        String oldname = names.get("oldname");
        String newname = names.get("newname");
        singerService.updateName(oldname, newname);
        return Result.success();
    }
    /*删除数据*/
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id){
        singerService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){//接受数组集合
        singerService.deleteBatch(ids);
        return Result.success();
    }
    @GetMapping("/selectAll")
    public Result selectAll(Singer singer){
        List<Singer> list = singerService.selectAll(singer);
        return Result.success(list);
    }
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Singer singer = singerService.selectById(id);
        return Result.success(singer);
    }
    @GetMapping("/selectType/{id}")
    public Result selectType(@PathVariable Integer id){
        String type = singerService.selectType(id);
        return Result.success(type);
    }


    @GetMapping("/selectByName/{name}")
    public Result selectByName(@PathVariable String name){
        Singer singer = singerService.selectByName(name);
        return Result.success(singer);
    }
    @GetMapping("/selectBySongsname/{name}")
    public Result selectBySongsname(@PathVariable String name, @RequestParam int pageNum, @RequestParam int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Singer> singerList = singerService.selectBySongsname(name, pageSize, offset);
        int total = singerService.countBySongsname(name);

        Map<String, Object> result = new HashMap<>();
        result.put("list", singerList);
        result.put("total", total);
        return Result.success(result);
    }
    @GetMapping("/selectPage")
    public Result selectPage(Singer singer, @RequestParam(defaultValue = "1")Integer pageNum, @RequestParam(defaultValue = "10")Integer pageSize){
        PageInfo<Singer> pageInfo = singerService.selectPage(singer,pageNum,pageSize);
        return Result.success(pageInfo);
    }
    @PutMapping("/modify")
    public Result modify(@RequestBody Account account){
        singerService.modify(account);
        return Result.success();
    }
    @GetMapping("/groupByType")
    public Result getSingerCountByType() {
        List<Singer> data = singerService.getSingerCountByType();
        return Result.success(data);
    }
}

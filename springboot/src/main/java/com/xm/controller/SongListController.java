package com.xm.controller;

import com.github.pagehelper.PageInfo;
import com.xm.common.Result;
import com.xm.entity.SongList;
import com.xm.service.SongListService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/songList")
public class SongListController {
    @Resource
    private SongListService songListService;
    /*新增数据*/
    @PostMapping("/add")
    public Result add(@RequestBody SongList songList){
        songListService.add(songList);
        return Result.success();
    }
    /*更新数据*/
    @PutMapping("/update")
    public Result update(@RequestBody SongList songList){
        songListService.update(songList);
        return Result.success();
    }
    /*删除数据*/
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id){
        songListService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){//接受数组集合
        songListService.deleteBatch(ids);
        return Result.success();
    }
    @GetMapping("/selectAll")
    public Result selectAll(SongList songList){
        List<SongList> list = songListService.selectAll(songList);
        return Result.success(list);
    }
    @GetMapping("/songListsCount")
    public Result selectSongList(SongList songList){
        List<SongList> list = songListService.selectSongList(songList);
        return Result.success(list);
    }
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        SongList songList = songListService.selectById(id);
        return Result.success(songList);
    }
    @GetMapping("/selectByCreateId/{id}")
    public Result selectByCreateId(@PathVariable Integer id){
        List<SongList> songList = songListService.selectByCreateId(id);
        return Result.success(songList);
    }

    @GetMapping("/selectByName/{name}")
    public Result selectByName(@PathVariable String name){
        SongList songList = songListService.selectByName(name);
        return Result.success(songList);
    }
    @GetMapping("/selectPage")
    public Result selectPage(
            SongList songList,
            @RequestParam(defaultValue = "1")Integer pageNum,
            @RequestParam(defaultValue = "10")Integer pageSize) {
        PageInfo<SongList> pageInfo = songListService.selectPage(songList, pageNum, pageSize);
        return Result.success(pageInfo);
    }


}

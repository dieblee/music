package com.xm.controller;

import com.github.pagehelper.PageInfo;
import com.xm.common.Result;
import com.xm.entity.Account;
import com.xm.entity.Singer;
import com.xm.entity.Song;
import com.xm.entity.User;
import com.xm.service.SingerService;
import com.xm.service.SongService;
import jakarta.annotation.Resource;
import org.apache.el.parser.AstPlus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/song")
public class SongController {
    @Resource
    private SongService songService;
    /*新增数据*/
    @PostMapping("/add")
    public Result add(@RequestBody Song song){
        songService.add(song);
        return Result.success();
    }
    /*更新数据*/
    @PutMapping("/update")
    public Result update(@RequestBody Song song){
        songService.update(song);
        return Result.success();
    }
    /*删除数据*/
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id){
        songService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){//接受数组集合
        songService.deleteBatch(ids);
        return Result.success();
    }
    @GetMapping("/selectAll")
    public Result selectAll(Song song){
        List<Song> list = songService.selectAll(song);
        return Result.success(list);
    }
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Song song = songService.selectById(id);
        return Result.success(song);
    }

    @GetMapping("/selectByName/{name}")
    public Result selectByName(@PathVariable String name){
        List<Song> songs = songService.selectByName(name);
        return Result.success(songs);
    }
    @GetMapping("/selectByAddress/{address}")
    public Result selectByAddress(@PathVariable String address){
        List<Song> song = songService.selectByAddress(address);
        return Result.success(song);
    }
    @GetMapping("/selectPage")
    public Result selectPage(
            Song song,
            @RequestParam(defaultValue = "1")Integer pageNum,
            @RequestParam(defaultValue = "10")Integer pageSize) {
        PageInfo<Song> pageInfo = songService.selectPage(song, pageNum, pageSize);
        return Result.success(pageInfo);
    }


}

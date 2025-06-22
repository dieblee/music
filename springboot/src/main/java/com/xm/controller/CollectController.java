package com.xm.controller;

import com.xm.common.Result;
import com.xm.entity.*;
import com.xm.service.CollectService;
import com.xm.service.SwiperService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collect")
public class CollectController {
    @Resource
    private CollectService collectService;
    /*新增数据*/
    @PostMapping("/add")
    public Result add(@RequestBody Collect collect){
        collectService.add(collect);
        return Result.success();
    }
    /*更新数据*/
    @PutMapping("/update")
    public Result update(@RequestBody Collect collect){
        collectService.update(collect);
        return Result.success();
    }
    /*删除数据*/
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id){
        collectService.deleteById(id);
        return Result.success();
    }
    @DeleteMapping("/removeBySongId/{useId}/{userId}")
    public Result deleteBySongId(@PathVariable Integer useId,@PathVariable Integer userId){
        collectService.removeBySongId(useId,userId);
        return Result.success();
    }
    @DeleteMapping("/removeBySongListId/{useId}/{userId}")
    public Result deleteById(@PathVariable Integer useId,@PathVariable Integer userId){
        collectService.removeBySongListId(useId,userId);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){//接受数组集合
        collectService.deleteBatch(ids);
        return Result.success();
    }
    @GetMapping("/selectAll")
    public Result selectAll(Collect collect){
        List<Collect> list = collectService.selectAll(collect);
        return Result.success(list);
    }
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Collect collect = collectService.selectById(id);
        return Result.success(collect);
    }
    @GetMapping("/selectSongListByUserId/{id}")
    public Result selectSongListByUserId(@PathVariable Integer id){
        List<SongList> songLists = collectService.selectSongListByUserId(id);
        return Result.success(songLists);
    }
    @GetMapping("/selectSongByUserId/{id}")
    public Result selectSongByUserId(@PathVariable Integer id){
        List<Song> songs = collectService.selectSongByUserId(id);
        return Result.success(songs);
    }
    @GetMapping("/selectCollectSong/{songId}/{userId}")
    public Result selectCollectSong(@PathVariable Integer songId,@PathVariable Integer userId){
        Collect collect = collectService.selectCollectSong(songId,userId);
        return Result.success(collect);
    }
    @GetMapping("/selectCollectSongList/{songListId}/{userId}")
    public Result selectCollectSongList(@PathVariable Integer songListId,@PathVariable Integer userId){
        Collect collect = collectService.selectCollectSongList(songListId,userId);
        return Result.success(collect);
    }

}

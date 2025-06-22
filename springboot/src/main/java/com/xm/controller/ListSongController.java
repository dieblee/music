package com.xm.controller;

import com.github.pagehelper.PageInfo;
import com.xm.common.Result;
import com.xm.entity.ListSong;
import com.xm.entity.Song;
import com.xm.exception.CustomException;
import com.xm.service.ListSongService;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/listsong")
public class ListSongController {
    @Resource
    private ListSongService lsService;
    /*新增数据*/
    @PostMapping("/add")
    public Result add(@RequestBody ListSong ls){
        lsService.add(ls);
        return Result.success();
    }
    /*更新数据*/
    @PutMapping("/update")
    public Result update(@RequestBody  ListSong ls){
        lsService.update(ls);
        return Result.success();
    }
    /*删除数据*/
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id){
        lsService.deleteById(id);
        return Result.success();
    }
    @DeleteMapping("/manageSongs/{songListId}/{songId}")
    public Result ManageSongs(@PathVariable("songListId") Integer songListId,
                              @PathVariable("songId") Integer songId){
        lsService.manageSongs(songListId,songId);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll( ListSong ls){
        List< ListSong> list = lsService.selectAll(ls);
        return Result.success(list);
    }

    @GetMapping("/selectAllSongs/{id}")
    public Result selectAllSongs(@PathVariable Integer id){
        List<Song> list = lsService.selectAllSongs( id);
        return Result.success(list);
    }
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        ListSong ls = lsService.selectById(id);
        return Result.success(ls);
    }
}


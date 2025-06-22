package com.xm.controller;


import com.github.pagehelper.PageInfo;
import com.xm.common.Result;
import com.xm.entity.Comment;
import com.xm.entity.Consumer;
import com.xm.exception.CustomException;
import com.xm.service.CommentService;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentService commentService;
    /*新增数据*/
    @PostMapping("/add")
    public Result add(@RequestBody Comment comment){
        commentService.add(comment);
        return Result.success();
    }
    /*更新数据*/
    @PutMapping("/update")
    public Result update(@RequestBody Comment comment){
        commentService.update(comment);
        return Result.success();
    }
    /*删除数据*/
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id){
        commentService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){//接受数组集合
        commentService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(Comment comment){
        List<Comment> list = commentService.selectAll(comment);
        return Result.success(list);
    }
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Comment comment = commentService.selectById(id);
        return Result.success(comment);
    }
    @GetMapping("/selectBySongListId/{song_list_id}")
    public Result selectBySongListId(@PathVariable Integer song_list_id){
        List<Comment> comment = commentService.selectBySongListId(song_list_id);
        return Result.success(comment);
    }
    @GetMapping("/selectBySongId/{song_id}")
    public Result selectBySongId(@PathVariable Integer song_id){
        List<Comment> comment = commentService.selectBySongId(song_id);
        return Result.success(comment);
    }
    @GetMapping("/selectPage")
    public Result selectPage(
            Comment comment,
            @RequestParam(defaultValue = "1")Integer pageNum,
            @RequestParam(defaultValue = "10")Integer pageSize){
        PageInfo<Comment> pageInfo = commentService.selectPage(comment,pageNum,pageSize);
        return Result.success(pageInfo);
    }
}


package com.xm.controller;

import com.github.pagehelper.PageInfo;
import com.xm.common.Result;
import com.xm.entity.Message;
import com.xm.entity.Song;
import com.xm.exception.CustomException;
import com.xm.mapper.MessageMapper;
import com.xm.service.ListSongService;
import com.xm.service.MessageService;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Resource
    private MessageService messageService;
    @Autowired
    private MessageMapper messageMapper;
    /*新增数据*/
    @PostMapping("/add")
    public Result add(@RequestBody Message message){
        messageService.add(message);
        return Result.success();
    }
    /*更新数据*/
    @PutMapping("/update")
    public Result update(@RequestBody  Message message){
        messageService.update(message);
        return Result.success();
    }


    /*删除数据*/
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id){
        messageService.deleteById(id);
        return Result.success();
    }
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){//接受数组集合
        messageService.deleteBatch(ids);
        return Result.success();
    }
    @PostMapping("/addBatch")
    public Result addBatch(@RequestBody Message dto) {
        for(Integer id : dto.getIds()){
            Message message = new Message();
            message.setReceiveId(id);
            message.setContent(dto.getContent());
            message.setSendId(dto.getSendId());
            messageMapper.insert(message);
        }
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll( Message message){
        List< Message> list = messageService.selectAll(message);
        return Result.success(list);
    }

    @GetMapping("/selectByReceiveId/{id}")
    public Result selectById(@PathVariable Integer id){
        List<Message> messages = messageService.selectByReceiveId(id);
        return Result.success(messages);
    }
    @GetMapping("/selectBySendId/{id}")
    public Result selectBySendId(@PathVariable Integer id){
        List<Message> messages = messageService.selectBySendId(id);
        return Result.success(messages);
    }
    @GetMapping("/selectPage")
    public Result selectPage(
            Message message,
            @RequestParam(defaultValue = "1")Integer pageNum,
            @RequestParam(defaultValue = "10")Integer pageSize) {
        PageInfo<Message> pageInfo = messageService.selectPage(message, pageNum, pageSize);
        return Result.success(pageInfo);
    }
}


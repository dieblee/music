package com.xm.controller;

import com.xm.common.Result;
import com.xm.entity.*;
import com.xm.service.StatisticService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistic")
public class StatisticController {
    @Resource
    private StatisticService statisticService;
    /*新增数据*/
    @PostMapping("/add")
    public Result add(@RequestBody Statistic statistic){
        statisticService.add(statistic);
        return Result.success();
    }
    /*更新数据*/
    @PutMapping("/update")
    public Result update(@RequestBody Statistic statistic){
        statisticService.update(statistic);
        return Result.success();
    }

    @GetMapping("/getClickCount/{id}")
    public Integer getClickCount(@PathVariable Integer id) {
        return statisticService.getClickCount(id);
    }
    @GetMapping("/getClickDate/{id}")
    public Map<String, String> getClickDate(@PathVariable Integer id) {
        Date date = statisticService.getClickDate(id);
        Map<String, String> response = new HashMap<>();
        if (date == null) {
            response.put("clickDate", "");  // 避免 null
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            response.put("clickDate", sdf.format(date));
        }
        return response;
    }
    @GetMapping("/getslClickDate/{id}")
    public Map<String, String> getslClickDate(@PathVariable Integer id) {
        Date date = statisticService.getslClickDate(id);
        Map<String, String> response = new HashMap<>();
        if (date == null) {
            response.put("clickDate", "");  // 避免 null
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            response.put("clickDate", sdf.format(date));
        }
        return response;
    }
    @GetMapping("/getsongsClick")
    public Integer countSongsclick(){
        return statisticService.countSongsclick();
    }
    @GetMapping("/getnewClicks/{today}")
    public Integer countnewclick(@PathVariable String today){
        return statisticService.getTodayNewClicks(today);
    }
    // 更新点击次数
    @PostMapping("/updateClickCount/{id}")
    public void updateClickCount(@PathVariable Integer id) {
        statisticService.updateClickCount(id);
    }
    @PostMapping("/updateOldClickCount/{id}")
    public void updateOldClickCount(@PathVariable Integer id) {
        statisticService.updateOldClickCount(id);
    }
    @PostMapping("/updateslOldClickCount/{id}")
    public void updateslOldClickCount(@PathVariable Integer id) {
        statisticService.updateslOldClickCount(id);
    }
    @PostMapping("/updateslClickCount/{id}")
    public void updateslClickCount(@PathVariable Integer id) {
        statisticService.updateslClickCount(id);
    }
    /*删除数据*/
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id){
        statisticService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){//接受数组集合
        statisticService.deleteBatch(ids);
        return Result.success();
    }
    @GetMapping("/selectAll")
    public Result selectAll(Statistic statistic){
        List<Statistic> list = statisticService.selectAll(statistic);
        return Result.success(list);
    }
    @GetMapping("/rankSong")
    public Result rankSong(){
        List<Statistic> list = statisticService.rankBysongclick();
        return Result.success(list);
    }
    @GetMapping("/rankSinger")
    public Result rankSinger(){
        List<Statistic> list = statisticService.rankBysingerclick();
        return Result.success(list);
    }
    @GetMapping("/rankSongList")
    public Result rankSongList(){
        List<Statistic> list = statisticService.rankBysonglistclick();
        return Result.success(list);
    }
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Statistic statistic = statisticService.selectById(id);
        return Result.success(statistic);
    }

}

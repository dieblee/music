package com.xm.controller;

import com.xm.common.Result;
import com.xm.entity.User;
import com.xm.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
public class WebController {
    @Resource
    private UserService userService;
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        User dbuser = userService.login(user);
        return Result.success( dbuser);
    }
    @PostMapping("/register")
    public Result register(@RequestBody User user){
        userService.register(user);
        return Result.success();
    }


}

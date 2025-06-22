package com.xm.exception;

import com.xm.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice("com.xm.controller")//监视controller
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody//返回json
    public Result error(Exception e){
        e.printStackTrace();
        return Result.error();
    }
    @ExceptionHandler(CustomException.class)
    @ResponseBody//返回json
    public Result error(CustomException e){
        e.printStackTrace();
        return Result.error(e.getCode(),e.getMsg());
    }
}

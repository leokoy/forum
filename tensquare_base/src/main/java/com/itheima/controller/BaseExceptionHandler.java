package com.itheima.controller;

import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

/**
 * 公共异常处理类
 * spring aop 程序抛出异常后，进行拦截，根据异常信息返回相应的错误信息
 */
//@ControllerAdvice //控制层apo拦截
@RestControllerAdvice //==ControllerAdvice+ResponseBody
public class BaseExceptionHandler {

    /**
     * 拦截后 返回统一定义错误信息
     */
    @ExceptionHandler(Exception.class) //所有异常都拦截
    //@ResponseBody
    public Result error(Exception e){
        String rsMessage = e.getMessage();
        //根据具体的异常 返回相应错误提示
        if(e instanceof NoSuchElementException){
            rsMessage = "数据查询不到";
        }
        return new Result(false, StatusCode.ERROR,rsMessage);
    }
}

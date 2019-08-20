package com.tensquare.friend.controller;

import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 交友微服务控制层
 */
@RequestMapping("/friend")
@RestController
@CrossOrigin
public class FriendContrller {
    @Autowired
    private FriendService friendService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 添加好友或非好友
     * friendid:好友id
     * type:类型 1:喜欢 2：不喜欢
     */
    @RequestMapping(value = "/like/{friendid}/{type}",method = RequestMethod.PUT)
    public Result like(@PathVariable String friendid,@PathVariable String type){
        Claims claims = (Claims) request.getAttribute("user_claims");
        if(claims == null || !claims.get("roles").equals("user")){
            return new Result(false, StatusCode.ERROR,"权限不足");
        }
        String userId = claims.getId();//当前登录的用户id
        //鉴权
        if(type.equals("1")){
            //往好友表插入记录
            friendService.like(userId,friendid);//userid:认证后userid friendid:地址栏中 islike=0
        }
        else {
            //往非好友表插入数据
            friendService.nolike(userId,friendid);
        }
        return new Result(true, StatusCode.OK,"操作成功");
    }
}

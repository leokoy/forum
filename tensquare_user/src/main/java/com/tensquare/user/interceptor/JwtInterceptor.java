package com.tensquare.user.interceptor;

import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 */
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {


    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 请求之前 统一进行鉴权
     *
     *     在拦截器中将处理结果存入request
     *       request.setAttribute("admin_claims",claims);
     *       request.setAttribute("user_claims",claims);
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("拦截器被访问了");
        //头信息Authorization ,内容为Bearer+空格+token
        String authorization = request.getHeader("Authorization");
        if(!StringUtils.isEmpty(authorization) && authorization.startsWith("Bearer ")){
            //提取token
            String token = authorization.substring(7);
            if(!StringUtils.isEmpty(token)){
                Claims claims = jwtUtil.parseJWT(token);
                if(claims != null ){
                    if(claims.get("roles").equals("admin")){
                        request.setAttribute("admin_claims",claims);
                    }

                    if(claims.get("roles").equals("user")){
                        request.setAttribute("user_claims",claims);
                    }
                }
            }
        }
        //不管是否有权限 都放行
        return true;
    }
}

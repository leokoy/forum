package com.tensquare.user.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 */
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    /**
     * 请求之前 统一进行鉴权
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        System.out.println("拦截器被访问了");
//        return true;
//    }
}

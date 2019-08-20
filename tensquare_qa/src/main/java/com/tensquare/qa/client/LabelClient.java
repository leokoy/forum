package com.tensquare.qa.client;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 1.引入服务调用组件依赖
 * 2.在启动类上加上两个注解 启用组件 启用远程访问
 * 3.新建接口 提供方法调用基础微服务
 *   @FeignClient:配置被调用的微服务名
 *   被调用微服务方法上的路径修改为 全路径
 *   如果有参数，必须加别名方式
 * 4.在控制层注入LabelClient接口，调用findById
 *
 *  测试注意点：
 *  1.先启动tensquare_eureka注册中心
 *  2.被调用方基础微服务tensquare_base
 *  3.再启动调用方tensquare_qa
 *
 *  访问测试：
 *  http://localhost/problem/mylabel/1
 */
@FeignClient("tensquare-base")
public interface LabelClient {

    /**
     * 根据标签id查询标签数据
     * @param labelId
     * @return
     */
    @RequestMapping(value = "/label/{labelId}",method = RequestMethod.GET)
    public Result findById(@PathVariable("labelId") String labelId);
}

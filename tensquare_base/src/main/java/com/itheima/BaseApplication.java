package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import util.IdWorker;

/**
 * 基础微服务启动类
 */
@SpringBootApplication
@EnableEurekaClient //启用组件 将当前微服务注册到注册中心
public class BaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class);
    }

    /**
     * 通过@Bean注解将idwork放spring容器中
     * 后续可以通过@Autowired注入idwork使用
     * @return
     */
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1, 1);
    }


}

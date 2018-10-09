package com.codinger.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {

    @RequestMapping("/testCache")
    @Cacheable(value="testCache")
    //@Cacheable  方法执行前先查看缓存中是否有数据，有，不执行方法体，直接返回缓存数据；
    //没有，调用方法体返回结果并将结果存入缓存
    public String testCache() {
    	System.out.println("方法体被执行");  
        return "123";
    }
}

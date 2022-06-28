package org.example.flowcontrol.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试类
 */
@RestController
public class TestController {

    @GetMapping("/hello")
    public String hello(){
        return "访问成功";
    }
}

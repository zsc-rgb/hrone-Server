package com.hrone.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试Controller
 * 
 * @author hrone
 */
@RestController
@RequestMapping("/test")
public class TestController {
    
    /**
     * 测试接口
     */
    @GetMapping("/hello")
    public Map<String, Object> hello() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "Hello, HROne!");
        result.put("data", "项目启动成功！");
        result.put("time", LocalDateTime.now());
        return result;
    }
    
    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "UP");
        result.put("message", "服务运行正常");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }
}


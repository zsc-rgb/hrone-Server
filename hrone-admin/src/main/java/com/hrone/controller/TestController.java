package com.hrone.controller;

import com.hrone.common.core.domain.AjaxResult;
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
     * 测试接口 - 使用AjaxResult
     */
    @GetMapping("/hello")
    public AjaxResult hello() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "项目启动成功！");
        data.put("time", LocalDateTime.now());
        data.put("version", "1.0.0");
        
        return AjaxResult.success("Hello, HROne!", data);
    }
    
    /**
     * 测试成功响应
     */
    @GetMapping("/success")
    public AjaxResult testSuccess() {
        return AjaxResult.success("操作成功");
    }
    
    /**
     * 测试成功响应带数据
     */
    @GetMapping("/success-data")
    public AjaxResult testSuccessData() {
        Map<String, Object> user = new HashMap<>();
        user.put("id", 1);
        user.put("name", "张三");
        user.put("age", 25);
        
        return AjaxResult.success("查询成功", user);
    }
    
    /**
     * 测试错误响应
     */
    @GetMapping("/error")
    public AjaxResult testError() {
        return AjaxResult.error("操作失败");
    }
    
    /**
     * 测试自定义状态码
     */
    @GetMapping("/custom")
    public AjaxResult testCustom() {
        return AjaxResult.error(601, "自定义警告消息");
    }
    
    /**
     * 测试链式调用
     */
    @GetMapping("/chain")
    public AjaxResult testChain() {
        return AjaxResult.success()
                .put("page", 1)
                .put("size", 10)
                .put("total", 100);
    }
    
    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public AjaxResult health() {
        return AjaxResult.success("服务运行正常")
                .put("status", "UP")
                .put("timestamp", System.currentTimeMillis());
    }
}

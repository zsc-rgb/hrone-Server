package com.hrone.controller;

import com.hrone.common.core.domain.AjaxResult;
import com.hrone.common.exception.ServiceException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 异常测试Controller
 * 
 * 用途：测试第2.4阶段实现的全局异常处理
 * 
 * @author hrone
 */
@RestController
@RequestMapping("/test/exception")
public class ExceptionTestController {
    
    /**
     * 测试业务异常（默认错误码500）
     * 
     * 访问地址：GET http://localhost:8080/test/exception/service
     * 
     * 预期结果：
     * {
     *   "code": 500,
     *   "msg": "用户名已存在"
     * }
     */
    @GetMapping("/service")
    public AjaxResult testServiceException() {
        // 模拟业务异常：用户名已存在
        throw new ServiceException("用户名已存在");
    }
    
    /**
     * 测试业务异常（自定义错误码）
     * 
     * 访问地址：GET http://localhost:8080/test/exception/service403
     * 
     * 预期结果：
     * {
     *   "code": 403,
     *   "msg": "没有权限访问该资源"
     * }
     */
    @GetMapping("/service403")
    public AjaxResult testServiceException403() {
        // 模拟权限不足异常
        throw new ServiceException("没有权限访问该资源", 403);
    }
    
    /**
     * 测试空指针异常
     * 
     * 访问地址：GET http://localhost:8080/test/exception/nullpointer
     * 
     * 预期结果：
     * {
     *   "code": 500,
     *   "msg": "系统运行异常：NullPointerException，请联系管理员"
     * }
     */
    @GetMapping("/nullpointer")
    public AjaxResult testNullPointerException() {
        // 故意制造空指针异常
        String str = null;
        return AjaxResult.success(str.length());
    }
    
    /**
     * 测试数组越界异常
     * 
     * 访问地址：GET http://localhost:8080/test/exception/arrayindex
     * 
     * 预期结果：
     * {
     *   "code": 500,
     *   "msg": "系统运行异常：ArrayIndexOutOfBoundsException，请联系管理员"
     * }
     */
    @GetMapping("/arrayindex")
    public AjaxResult testArrayIndexOutOfBoundsException() {
        // 故意制造数组越界异常
        int[] arr = {1, 2, 3};
        return AjaxResult.success(arr[10]);
    }
    
    /**
     * 测试除零异常
     * 
     * 访问地址：GET http://localhost:8080/test/exception/arithmetic
     * 
     * 预期结果：
     * {
     *   "code": 500,
     *   "msg": "系统运行异常：ArithmeticException，请联系管理员"
     * }
     */
    @GetMapping("/arithmetic")
    public AjaxResult testArithmeticException() {
        // 故意制造除零异常
        int result = 10 / 0;
        return AjaxResult.success(result);
    }
    
    /**
     * 测试类型转换异常
     * 
     * 访问地址：GET http://localhost:8080/test/exception/classcast
     * 
     * 预期结果：
     * {
     *   "code": 500,
     *   "msg": "系统运行异常：ClassCastException，请联系管理员"
     * }
     */
    @GetMapping("/classcast")
    public AjaxResult testClassCastException() {
        // 故意制造类型转换异常
        Object obj = "字符串";
        Integer num = (Integer) obj;
        return AjaxResult.success(num);
    }
    
    /**
     * 测试正常业务（对比）
     * 
     * 访问地址：GET http://localhost:8080/test/exception/normal/{id}
     * 
     * 说明：
     * - 正常情况返回用户信息
     * - id不存在时抛出ServiceException
     */
    @GetMapping("/normal/{id}")
    public AjaxResult testNormalBusiness(@PathVariable Long id) {
        // 模拟业务逻辑
        if (id <= 0) {
            throw new ServiceException("用户ID必须大于0", 400);
        }
        
        if (id > 1000) {
            throw new ServiceException("用户不存在", 404);
        }
        
        // 正常返回
        Map<String, Object> user = new HashMap<>();
        user.put("userId", id);
        user.put("userName", "用户" + id);
        user.put("status", "正常");
        
        return AjaxResult.success("查询成功", user);
    }
    
    /**
     * 测试异常链（异常包装）
     * 
     * 访问地址：GET http://localhost:8080/test/exception/chain
     * 
     * 说明：
     * 底层异常被包装成ServiceException抛出
     */
    @GetMapping("/chain")
    public AjaxResult testExceptionChain() {
        try {
            // 模拟底层操作异常
            int result = 10 / 0;
            return AjaxResult.success(result);
        } catch (Exception e) {
            // 将底层异常包装成业务异常
            throw new ServiceException("数据处理失败", 500, e.getMessage());
        }
    }
    
    /**
     * 测试首页（显示所有测试接口）
     * 
     * 访问地址：GET http://localhost:8080/test/exception
     */
    @GetMapping
    public AjaxResult index() {
        Map<String, String> apis = new HashMap<>();
        apis.put("业务异常（默认500）", "GET /test/exception/service");
        apis.put("业务异常（自定义403）", "GET /test/exception/service403");
        apis.put("空指针异常", "GET /test/exception/nullpointer");
        apis.put("数组越界异常", "GET /test/exception/arrayindex");
        apis.put("除零异常", "GET /test/exception/arithmetic");
        apis.put("类型转换异常", "GET /test/exception/classcast");
        apis.put("正常业务测试", "GET /test/exception/normal/{id}");
        apis.put("异常链测试", "GET /test/exception/chain");
        
        return AjaxResult.success("异常处理测试接口列表", apis);
    }
}


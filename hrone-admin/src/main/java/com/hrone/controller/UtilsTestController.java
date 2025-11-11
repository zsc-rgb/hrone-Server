package com.hrone.controller;

import com.hrone.common.core.domain.AjaxResult;
import com.hrone.common.utils.DateUtils;
import com.hrone.common.utils.ServletUtils;
import com.hrone.common.utils.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 工具类测试Controller
 * 
 * 用途：测试第2.2阶段实现的三个工具类
 * 
 * @author hrone
 */
@RestController
@RequestMapping("/test/utils")
public class UtilsTestController {
    
    /**
     * 测试 StringUtils 工具类
     * 
     * 访问地址：GET http://localhost:8080/test/utils/string
     */
    @GetMapping("/string")
    public AjaxResult testStringUtils() {
        Map<String, Object> result = new HashMap<>();
        
        // 1. 测试字符串判空
        result.put("isEmpty_null", StringUtils.isEmpty((CharSequence) null));  // true
        result.put("isEmpty_empty", StringUtils.isEmpty(""));                  // true
        result.put("isEmpty_blank", StringUtils.isEmpty(" "));                 // false
        result.put("isBlank_blank", StringUtils.isBlank(" "));                 // true
        
        // 2. 测试字符串格式化
        String formatted = StringUtils.format("用户{}在{}登录成功", "张三", DateUtils.formatDateTime(new Date()));
        result.put("format", formatted);
        
        // 3. 测试驼峰转下划线
        result.put("toUnderScoreCase_userName", StringUtils.toUnderScoreCase("userName"));    // user_name
        result.put("toUnderScoreCase_userId", StringUtils.toUnderScoreCase("userId"));        // user_id
        result.put("toUnderScoreCase_createTime", StringUtils.toUnderScoreCase("createTime")); // create_time
        
        // 4. 测试字符串截取
        String text = "Hello World";
        result.put("substring_0_5", StringUtils.substring(text, 0, 5));  // Hello
        result.put("substring_6", StringUtils.substring(text, 6));       // World
        
        return AjaxResult.success("StringUtils 测试完成", result);
    }
    
    /**
     * 测试 DateUtils 工具类
     * 
     * 访问地址：GET http://localhost:8080/test/utils/date
     */
    @GetMapping("/date")
    public AjaxResult testDateUtils() {
        Map<String, Object> result = new HashMap<>();
        
        Date now = new Date();
        
        // 1. 测试日期格式化
        result.put("formatDateTime", DateUtils.formatDateTime(now));           // yyyy-MM-dd HH:mm:ss
        result.put("formatDate", DateUtils.formatDate(now));                   // yyyy-MM-dd
        result.put("format_custom", DateUtils.format(now, "yyyy年MM月dd日"));   // 自定义格式
        
        // 2. 测试日期解析
        Date parsed = DateUtils.parseDate("2025-11-05");
        result.put("parseDate", DateUtils.formatDate(parsed));
        
        // 3. 测试日期计算
        Date tomorrow = DateUtils.addDays(now, 1);
        Date yesterday = DateUtils.addDays(now, -1);
        Date nextMonth = DateUtils.addMonths(now, 1);
        Date nextYear = DateUtils.addYears(now, 1);
        
        result.put("tomorrow", DateUtils.formatDate(tomorrow));
        result.put("yesterday", DateUtils.formatDate(yesterday));
        result.put("nextMonth", DateUtils.formatDate(nextMonth));
        result.put("nextYear", DateUtils.formatDate(nextYear));
        
        // 4. 测试日期间隔
        Date start = DateUtils.parseDate("2025-11-01");
        Date end = DateUtils.parseDate("2025-11-05");
        long days = DateUtils.daysBetween(start, end);
        result.put("daysBetween", days);  // 4
        
        // 5. 测试当天开始和结束时间
        result.put("startOfDay", DateUtils.formatDateTime(DateUtils.getStartOfDay()));
        result.put("endOfDay", DateUtils.formatDateTime(DateUtils.getEndOfDay()));
        
        return AjaxResult.success("DateUtils 测试完成", result);
    }
    
    /**
     * 测试 ServletUtils 工具类
     * 
     * 访问地址：GET http://localhost:8080/test/utils/servlet?username=zhangsan&age=25&active=true
     */
    @GetMapping("/servlet")
    public AjaxResult testServletUtils() {
        Map<String, Object> result = new HashMap<>();
        
        // 1. 测试获取请求参数
        String username = ServletUtils.getParameter("username", "默认用户");
        Integer age = ServletUtils.getParameterToInt("age", 0);
        Boolean active = ServletUtils.getParameterToBool("active", false);
        
        result.put("username", username);
        result.put("age", age);
        result.put("active", active);
        
        // 2. 测试获取请求信息
        result.put("clientIP", ServletUtils.getClientIP());
        result.put("method", ServletUtils.getMethod());
        result.put("requestURL", ServletUtils.getRequestURL());
        result.put("userAgent", ServletUtils.getUserAgent());
        result.put("isAjax", ServletUtils.isAjaxRequest());
        
        return AjaxResult.success("ServletUtils 测试完成", result);
    }
    
    /**
     * 综合测试：实际业务场景模拟
     * 
     * 场景：用户登录日志记录
     * 访问地址：GET http://localhost:8080/test/utils/comprehensive?username=zhangsan
     */
    @GetMapping("/comprehensive")
    public AjaxResult comprehensiveTest() {
        // 1. 获取用户名（使用 ServletUtils）
        String username = ServletUtils.getParameter("username");
        if (StringUtils.isEmpty(username)) {
            return AjaxResult.error("用户名不能为空");
        }
        
        // 2. 获取登录时间（使用 DateUtils）
        String loginTime = DateUtils.formatDateTime(new Date());
        
        // 3. 获取客户端信息（使用 ServletUtils）
        String clientIP = ServletUtils.getClientIP();
        String userAgent = ServletUtils.getUserAgent();
        
        // 4. 构造日志消息（使用 StringUtils）
        String logMessage = StringUtils.format(
            "用户[{}]于{}从IP[{}]登录系统",
            username, loginTime, clientIP
        );
        
        // 5. 返回结果
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("loginTime", loginTime);
        data.put("clientIP", clientIP);
        data.put("userAgent", userAgent);
        data.put("logMessage", logMessage);
        
        // 模拟数据库字段名转换
        data.put("dbFieldName_userName", StringUtils.toUnderScoreCase("userName"));
        data.put("dbFieldName_loginTime", StringUtils.toUnderScoreCase("loginTime"));
        
        return AjaxResult.success("用户登录成功", data);
    }
    
    /**
     * 测试首页 - 显示所有测试接口
     * 
     * 访问地址：GET http://localhost:8080/test/utils
     */
    @GetMapping
    public AjaxResult index() {
        Map<String, String> apis = new HashMap<>();
        apis.put("StringUtils测试", "GET /test/utils/string");
        apis.put("DateUtils测试", "GET /test/utils/date");
        apis.put("ServletUtils测试", "GET /test/utils/servlet?username=zhangsan&age=25&active=true");
        apis.put("综合测试", "GET /test/utils/comprehensive?username=zhangsan");
        
        return AjaxResult.success("工具类测试接口列表", apis);
    }

}


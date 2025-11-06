package com.hrone.controller;

import com.hrone.common.constant.CacheConstants;
import com.hrone.common.constant.Constants;
import com.hrone.common.constant.UserConstants;
import com.hrone.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量测试Controller
 * 
 * 用途：演示第2.5阶段实现的常量类的使用
 * 
 * @author hrone
 */
@RestController
@RequestMapping("/test/constants")
public class ConstantsTestController {
    
    /**
     * 测试通用常量
     * 
     * 访问地址：GET http://localhost:8080/test/constants/common
     */
    @GetMapping("/common")
    public AjaxResult testCommonConstants() {
        Map<String, Object> result = new HashMap<>();
        
        // 1. 字符集常量
        result.put("UTF8", Constants.UTF8);
        result.put("GBK", Constants.GBK);
        
        // 2. 协议常量
        result.put("HTTP", Constants.HTTP);
        result.put("HTTPS", Constants.HTTPS);
        
        // 3. 状态码常量
        result.put("SUCCESS", Constants.SUCCESS);
        result.put("FAIL", Constants.FAIL);
        
        // 4. 登录状态常量
        result.put("LOGIN_SUCCESS", Constants.LOGIN_SUCCESS);
        result.put("LOGIN_FAIL", Constants.LOGIN_FAIL);
        result.put("LOGOUT", Constants.LOGOUT);
        
        // 5. Token常量
        result.put("TOKEN", Constants.TOKEN);
        result.put("TOKEN_PREFIX", Constants.TOKEN_PREFIX);
        result.put("LOGIN_USER_KEY", Constants.LOGIN_USER_KEY);
        
        // 6. 验证码常量
        result.put("CAPTCHA_EXPIRATION_MINUTES", Constants.CAPTCHA_EXPIRATION);
        
        return AjaxResult.success("通用常量列表", result);
    }
    
    /**
     * 测试用户常量
     * 
     * 访问地址：GET http://localhost:8080/test/constants/user
     */
    @GetMapping("/user")
    public AjaxResult testUserConstants() {
        Map<String, Object> result = new HashMap<>();
        
        // 1. 用户状态常量
        result.put("USER_NORMAL", UserConstants.USER_NORMAL);
        result.put("USER_DISABLE", UserConstants.USER_DISABLE);
        
        // 2. 角色状态常量
        result.put("ROLE_NORMAL", UserConstants.ROLE_NORMAL);
        result.put("ROLE_DISABLE", UserConstants.ROLE_DISABLE);
        
        // 3. 部门状态常量
        result.put("DEPT_NORMAL", UserConstants.DEPT_NORMAL);
        result.put("DEPT_DISABLE", UserConstants.DEPT_DISABLE);
        
        // 4. 菜单状态常量
        result.put("MENU_NORMAL", UserConstants.MENU_NORMAL);
        result.put("MENU_DISABLE", UserConstants.MENU_DISABLE);
        
        // 5. 菜单类型常量
        result.put("TYPE_DIR", UserConstants.TYPE_DIR);
        result.put("TYPE_MENU", UserConstants.TYPE_MENU);
        result.put("TYPE_BUTTON", UserConstants.TYPE_BUTTON);
        
        // 6. 是否标识常量
        result.put("YES", UserConstants.YES);
        result.put("NO", UserConstants.NO);
        
        // 7. 唯一性校验常量
        result.put("UNIQUE", UserConstants.UNIQUE);
        result.put("NOT_UNIQUE", UserConstants.NOT_UNIQUE);
        
        // 8. 长度限制常量
        result.put("USERNAME_MIN_LENGTH", UserConstants.USERNAME_MIN_LENGTH);
        result.put("USERNAME_MAX_LENGTH", UserConstants.USERNAME_MAX_LENGTH);
        result.put("PASSWORD_MIN_LENGTH", UserConstants.PASSWORD_MIN_LENGTH);
        result.put("PASSWORD_MAX_LENGTH", UserConstants.PASSWORD_MAX_LENGTH);
        
        // 9. 超级管理员ID
        result.put("ADMIN_ID", UserConstants.ADMIN_ID);
        
        return AjaxResult.success("用户常量列表", result);
    }
    
    /**
     * 测试缓存常量
     * 
     * 访问地址：GET http://localhost:8080/test/constants/cache
     */
    @GetMapping("/cache")
    public AjaxResult testCacheConstants() {
        Map<String, Object> cacheKeys = new HashMap<>();
        Map<String, Object> expirations = new HashMap<>();
        
        // 1. 缓存Key前缀
        cacheKeys.put("LOGIN_TOKEN_KEY", CacheConstants.LOGIN_TOKEN_KEY);
        cacheKeys.put("CAPTCHA_CODE_KEY", CacheConstants.CAPTCHA_CODE_KEY);
        cacheKeys.put("SYS_CONFIG_KEY", CacheConstants.SYS_CONFIG_KEY);
        cacheKeys.put("SYS_DICT_KEY", CacheConstants.SYS_DICT_KEY);
        cacheKeys.put("REPEAT_SUBMIT_KEY", CacheConstants.REPEAT_SUBMIT_KEY);
        cacheKeys.put("RATE_LIMIT_KEY", CacheConstants.RATE_LIMIT_KEY);
        cacheKeys.put("PWD_ERR_CNT_KEY", CacheConstants.PWD_ERR_CNT_KEY);
        cacheKeys.put("USER_INFO_KEY", CacheConstants.USER_INFO_KEY);
        cacheKeys.put("MENU_INFO_KEY", CacheConstants.MENU_INFO_KEY);
        cacheKeys.put("PERMISSION_KEY", CacheConstants.PERMISSION_KEY);
        
        // 2. 过期时间（分钟）
        expirations.put("LOGIN_TOKEN_EXPIRATION", CacheConstants.LOGIN_TOKEN_EXPIRATION);
        expirations.put("CAPTCHA_EXPIRATION", CacheConstants.CAPTCHA_EXPIRATION);
        expirations.put("CACHE_EXPIRATION", CacheConstants.CACHE_EXPIRATION);
        expirations.put("DICT_CACHE_EXPIRATION", CacheConstants.DICT_CACHE_EXPIRATION);
        expirations.put("CONFIG_CACHE_EXPIRATION", CacheConstants.CONFIG_CACHE_EXPIRATION);
        expirations.put("PWD_ERR_CNT_EXPIRATION", CacheConstants.PWD_ERR_CNT_EXPIRATION);
        
        // 3. 其他配置
        Map<String, Object> configs = new HashMap<>();
        configs.put("PWD_ERR_MAX_COUNT", CacheConstants.PWD_ERR_MAX_COUNT);
        configs.put("REPEAT_SUBMIT_EXPIRATION", CacheConstants.REPEAT_SUBMIT_EXPIRATION);
        
        return AjaxResult.success("缓存常量列表")
                .put("cacheKeys", cacheKeys)
                .put("expirations", expirations)
                .put("configs", configs);
    }
    
    /**
     * 演示常量的实际使用
     * 
     * 访问地址：GET http://localhost:8080/test/constants/usage
     */
    @GetMapping("/usage")
    public AjaxResult testUsage() {
        Map<String, Object> examples = new HashMap<>();
        
        // 示例1：判断用户状态
        String userStatus = "0";
        boolean isNormalUser = UserConstants.USER_NORMAL.equals(userStatus);
        examples.put("示例1_用户状态正常", isNormalUser);
        
        // 示例2：生成缓存Key
        String userId = "10001";
        String cacheKey = CacheConstants.LOGIN_TOKEN_KEY + userId;
        examples.put("示例2_登录Token缓存Key", cacheKey);
        
        // 示例3：验证用户名长度
        String userName = "张三";
        boolean isValidLength = userName.length() >= UserConstants.USERNAME_MIN_LENGTH 
                             && userName.length() <= UserConstants.USERNAME_MAX_LENGTH;
        examples.put("示例3_用户名长度有效", isValidLength);
        
        // 示例4：构建完整URL
        String domain = "example.com";
        String fullUrl = Constants.HTTPS + domain;
        examples.put("示例4_完整URL", fullUrl);
        
        // 示例5：判断是否为超级管理员
        Long currentUserId = 1L;
        boolean isAdmin = UserConstants.ADMIN_ID.equals(currentUserId);
        examples.put("示例5_是否超级管理员", isAdmin);
        
        // 示例6：菜单类型判断
        String menuType = "M";
        boolean isDirectory = UserConstants.TYPE_DIR.equals(menuType);
        examples.put("示例6_是否为目录", isDirectory);
        
        return AjaxResult.success("常量使用示例", examples);
    }
    
    /**
     * 对比：使用常量 vs 魔法值
     * 
     * 访问地址：GET http://localhost:8080/test/constants/comparison
     */
    @GetMapping("/comparison")
    public AjaxResult testComparison() {
        Map<String, Object> result = new HashMap<>();
        
        // ❌ 不好的写法（魔法值）
        Map<String, String> badCode = new HashMap<>();
        badCode.put("示例1", "if (status.equals(\"0\"))");  // "0"是什么意思？
        badCode.put("示例2", "redisCache.set(\"user:\" + id)");  // 硬编码
        badCode.put("示例3", "if (userId == 1)");  // 1是什么意思？
        badCode.put("示例4", "if (type.equals(\"M\"))");  // M是什么？
        
        // ✅ 好的写法（使用常量）
        Map<String, String> goodCode = new HashMap<>();
        goodCode.put("示例1", "if (UserConstants.USER_NORMAL.equals(status))");
        goodCode.put("示例2", "redisCache.set(CacheConstants.USER_INFO_KEY + id)");
        goodCode.put("示例3", "if (UserConstants.ADMIN_ID.equals(userId))");
        goodCode.put("示例4", "if (UserConstants.TYPE_DIR.equals(type))");
        
        result.put("魔法值写法（不推荐）", badCode);
        result.put("常量写法（推荐）", goodCode);
        
        return AjaxResult.success("代码对比", result);
    }
    
    /**
     * 测试首页（显示所有测试接口）
     * 
     * 访问地址：GET http://localhost:8080/test/constants
     */
    @GetMapping
    public AjaxResult index() {
        Map<String, String> apis = new HashMap<>();
        apis.put("通用常量", "GET /test/constants/common");
        apis.put("用户常量", "GET /test/constants/user");
        apis.put("缓存常量", "GET /test/constants/cache");
        apis.put("使用示例", "GET /test/constants/usage");
        apis.put("代码对比", "GET /test/constants/comparison");
        
        return AjaxResult.success("常量测试接口列表", apis);
    }
}


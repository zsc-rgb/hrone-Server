package com.hrone.common.constant;

/**
 * 通用常量
 * 
 * 功能说明：
 * 1. 定义系统中使用的通用常量
 * 2. 消除魔法值，提高代码可读性
 * 3. 统一常量管理，便于维护
 * 
 * 使用场景：
 * - 替代代码中的硬编码字符串和数字
 * - 提高代码可维护性
 * - 统一常量命名规范
 * 
 * 命名规范：
 * - 全部大写
 * - 单词间用下划线分隔
 * - 见名知意
 * 
 * @author hrone
 */
public class Constants {
    
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";
    
    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";
    
    /**
     * www主域
     */
    public static final String WWW = "www.";
    
    /**
     * http请求
     */
    public static final String HTTP = "http://";
    
    /**
     * https请求
     */
    public static final String HTTPS = "https://";
    
    /**
     * 成功标记
     */
    public static final Integer SUCCESS = 200;
    
    /**
     * 失败标记
     */
    public static final Integer FAIL = 500;
    
    /**
     * 登录成功状态
     */
    public static final String LOGIN_SUCCESS_STATUS = "0";
    
    /**
     * 登录失败状态
     */
    public static final String LOGIN_FAIL_STATUS = "1";
    
    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";
    
    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";
    
    /**
     * 注册
     */
    public static final String REGISTER = "Register";
    
    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";
    
    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;
    
    /**
     * 令牌
     */
    public static final String TOKEN = "token";
    
    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";
    
    /**
     * 令牌前缀（LOGIN_USER）
     */
    public static final String LOGIN_USER_KEY = "login_user_key";
    
    /**
     * 用户ID
     */
    public static final String JWT_USERID = "userid";
    
    /**
     * 用户名称
     */
    public static final String JWT_USERNAME = "username";
    
    /**
     * 用户头像
     */
    public static final String JWT_AVATAR = "avatar";
    
    /**
     * 创建时间
     */
    public static final String JWT_CREATED = "created";
    
    /**
     * 用户权限
     */
    public static final String JWT_AUTHORITIES = "authorities";
    
    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";
    
    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";
    
    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";
    
    /**
     * RMI 远程方法调用
     */
    public static final String LOOKUP_RMI = "rmi:";
    
    /**
     * LDAP 远程方法调用
     */
    public static final String LOOKUP_LDAP = "ldap:";
    
    /**
     * LDAPS 远程方法调用
     */
    public static final String LOOKUP_LDAPS = "ldaps:";
    
    /**
     * 定时任务白名单配置（仅允许访问的包名，如其他需要可以自行添加）
     */
    public static final String[] JOB_WHITELIST_STR = { "com.hrone" };
    
    /**
     * 定时任务违规的字符
     */
    public static final String[] JOB_ERROR_STR = { "java.net.URL", "javax.naming.InitialContext", 
        "org.yaml.snakeyaml", "org.springframework", "org.apache" };
}


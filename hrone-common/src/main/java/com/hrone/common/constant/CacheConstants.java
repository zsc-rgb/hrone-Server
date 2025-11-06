package com.hrone.common.constant;

/**
 * 缓存常量
 * 
 * 功能说明：
 * 1. 定义Redis缓存的Key前缀
 * 2. 统一缓存过期时间
 * 3. 便于缓存管理和维护
 * 
 * 使用场景：
 * - Redis缓存Key的命名
 * - 缓存过期时间设置
 * - 缓存分类管理
 * 
 * 命名规范：
 * - Key前缀以模块命名（如 login:、captcha:、user:）
 * - 过期时间以 EXPIRATION 结尾
 * - 全部大写，下划线分隔
 * 
 * @author hrone
 */
public class CacheConstants {
    
    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";
    
    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";
    
    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";
    
    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";
    
    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";
    
    /**
     * 限流 redis key
     */
    public static final String RATE_LIMIT_KEY = "rate_limit:";
    
    /**
     * 登录账户密码错误次数 redis key
     */
    public static final String PWD_ERR_CNT_KEY = "pwd_err_cnt:";
    
    /**
     * 用户信息 cache key
     */
    public static final String USER_INFO_KEY = "user_info:";
    
    /**
     * 菜单信息 cache key
     */
    public static final String MENU_INFO_KEY = "menu_info:";
    
    /**
     * 权限信息 cache key
     */
    public static final String PERMISSION_KEY = "permission:";
    
    /**
     * 角色信息 cache key
     */
    public static final String ROLE_INFO_KEY = "role_info:";
    
    /**
     * 部门信息 cache key
     */
    public static final String DEPT_INFO_KEY = "dept_info:";
    
    /**
     * 登录token过期时间（分钟）
     * 默认30分钟
     */
    public static final Integer LOGIN_TOKEN_EXPIRATION = 30;
    
    /**
     * 验证码有效期（分钟）
     * 默认2分钟
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;
    
    /**
     * 缓存有效期（分钟）
     * 默认60分钟
     */
    public static final Integer CACHE_EXPIRATION = 60;
    
    /**
     * 字典缓存有效期（小时）
     * 默认24小时
     */
    public static final Integer DICT_CACHE_EXPIRATION = 24 * 60;
    
    /**
     * 配置缓存有效期（小时）
     * 默认12小时
     */
    public static final Integer CONFIG_CACHE_EXPIRATION = 12 * 60;
    
    /**
     * 密码错误次数缓存有效期（分钟）
     * 默认10分钟
     */
    public static final Integer PWD_ERR_CNT_EXPIRATION = 10;
    
    /**
     * 密码错误最大次数
     * 超过此次数将锁定账户
     */
    public static final Integer PWD_ERR_MAX_COUNT = 5;
    
    /**
     * 防重提交过期时间（秒）
     * 默认10秒
     */
    public static final Integer REPEAT_SUBMIT_EXPIRATION = 10;
}


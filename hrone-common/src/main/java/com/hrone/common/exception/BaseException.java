package com.hrone.common.exception;

/**
 * 基础异常类
 * 
 * 功能说明：
 * 1. 所有自定义异常的父类
 * 2. 扩展了异常信息的存储（模块、错误码、详细信息等）
 * 3. 支持国际化（预留message key）
 * 
 * 技术要点：
 * - 继承 RuntimeException（运行时异常，不需要强制catch）
 * - 提供多种构造方法，方便使用
 * - 存储额外的异常信息（模块、错误码等）
 * 
 * 异常层次：
 * BaseException（基础异常）
 *   ├── ServiceException（业务异常）
 *   ├── DemoException（演示异常）
 *   └── 其他自定义异常...
 * 
 * @author hrone
 */
public class BaseException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 所属模块
     * 例如：user、role、menu
     */
    private String module;
    
    /**
     * 错误码
     * 例如：USER_NOT_EXIST、PARAM_ERROR
     */
    private String code;
    
    /**
     * 错误码对应的参数
     * 用于国际化消息的参数替换
     */
    private Object[] args;
    
    /**
     * 默认错误消息
     * 不使用国际化时的默认消息
     */
    private String defaultMessage;
    
    /**
     * 无参构造方法
     */
    public BaseException() {
        super();
    }
    
    /**
     * 构造方法
     * 
     * @param message 错误消息
     */
    public BaseException(String message) {
        super(message);
        this.defaultMessage = message;
    }
    
    /**
     * 构造方法
     * 
     * @param message 错误消息
     * @param cause 原始异常
     */
    public BaseException(String message, Throwable cause) {
        super(message, cause);
        this.defaultMessage = message;
    }
    
    /**
     * 构造方法
     * 
     * @param module 所属模块
     * @param code 错误码
     * @param args 错误码参数
     */
    public BaseException(String module, String code, Object[] args) {
        this(module, code, args, null);
    }
    
    /**
     * 构造方法
     * 
     * @param module 所属模块
     * @param code 错误码
     * @param args 错误码参数
     * @param defaultMessage 默认错误消息
     */
    public BaseException(String module, String code, Object[] args, String defaultMessage) {
        super(defaultMessage);
        this.module = module;
        this.code = code;
        this.args = args;
        this.defaultMessage = defaultMessage;
    }
    
    /**
     * 获取所属模块
     */
    public String getModule() {
        return module;
    }
    
    /**
     * 设置所属模块
     */
    public BaseException setModule(String module) {
        this.module = module;
        return this;
    }
    
    /**
     * 获取错误码
     */
    public String getCode() {
        return code;
    }
    
    /**
     * 设置错误码
     */
    public BaseException setCode(String code) {
        this.code = code;
        return this;
    }
    
    /**
     * 获取错误码参数
     */
    public Object[] getArgs() {
        return args;
    }
    
    /**
     * 设置错误码参数
     */
    public BaseException setArgs(Object[] args) {
        this.args = args;
        return this;
    }
    
    /**
     * 获取默认错误消息
     */
    public String getDefaultMessage() {
        return defaultMessage;
    }
    
    /**
     * 设置默认错误消息
     */
    public BaseException setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
        return this;
    }
    
    @Override
    public String getMessage() {
        String message = null;
        
        // 优先使用 defaultMessage
        if (defaultMessage != null) {
            message = defaultMessage;
        }
        
        // 如果没有 defaultMessage，使用父类的 message
        if (message == null) {
            message = super.getMessage();
        }
        
        return message;
    }
}


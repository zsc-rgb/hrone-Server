package com.hrone.common.exception;

/**
 * 业务异常
 * 
 * 功能说明：
 * 1. 用于业务逻辑中的异常处理
 * 2. 可以携带错误码和详细信息
 * 3. 由全局异常处理器统一处理
 * 
 * 技术要点：
 * - 继承 RuntimeException，不需要强制捕获
 * - 用于可预见的业务异常（如用户不存在、权限不足等）
 * - 不用于系统异常（如数据库连接失败、网络异常等）
 * 
 * 使用场景：
 * - 用户名已存在
 * - 密码错误
 * - 权限不足
 * - 数据不存在
 * - 操作失败
 * 
 * 使用示例：
 * if (user == null) {
 *     throw new ServiceException("用户不存在");
 * }
 * 
 * if (!hasPermission) {
 *     throw new ServiceException("权限不足", 403);
 * }
 * 
 * @author hrone
 */
public class ServiceException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 错误码
     * 默认 500
     */
    private Integer code;
    
    /**
     * 错误提示
     */
    private String message;
    
    /**
     * 错误明细，内部调试错误
     */
    private String detailMessage;
    
    /**
     * 空构造方法，避免反序列化问题
     */
    public ServiceException() {
    }
    
    /**
     * 构造方法
     * 
     * @param message 错误消息
     * 
     * 示例：
     * throw new ServiceException("用户不存在");
     */
    public ServiceException(String message) {
        this.message = message;
    }
    
    /**
     * 构造方法
     * 
     * @param message 错误消息
     * @param code 错误码
     * 
     * 示例：
     * throw new ServiceException("权限不足", 403);
     */
    public ServiceException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }
    
    /**
     * 构造方法
     * 
     * @param message 错误消息
     * @param code 错误码
     * @param detailMessage 详细错误信息
     * 
     * 示例：
     * throw new ServiceException("数据库操作失败", 500, e.getMessage());
     */
    public ServiceException(String message, Integer code, String detailMessage) {
        this.message = message;
        this.code = code;
        this.detailMessage = detailMessage;
    }
    
    /**
     * 获取错误消息
     */
    @Override
    public String getMessage() {
        return message;
    }
    
    /**
     * 设置错误消息
     */
    public ServiceException setMessage(String message) {
        this.message = message;
        return this;
    }
    
    /**
     * 获取错误码
     */
    public Integer getCode() {
        return code;
    }
    
    /**
     * 设置错误码
     */
    public ServiceException setCode(Integer code) {
        this.code = code;
        return this;
    }
    
    /**
     * 获取详细错误信息
     */
    public String getDetailMessage() {
        return detailMessage;
    }
    
    /**
     * 设置详细错误信息
     */
    public ServiceException setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }
}


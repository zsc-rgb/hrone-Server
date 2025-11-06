package com.hrone.framework.exception;

import com.hrone.common.constant.HttpStatus;
import com.hrone.common.core.domain.AjaxResult;
import com.hrone.common.exception.ServiceException;
import com.hrone.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 
 * 功能说明：
 * 1. 统一处理所有Controller抛出的异常
 * 2. 将异常转换为统一的响应格式
 * 3. 记录异常日志，便于排查问题
 * 
 * 技术要点：
 * - @RestControllerAdvice：全局异常处理注解
 * - @ExceptionHandler：指定处理的异常类型
 * - 返回 AjaxResult 统一响应格式
 * - 记录日志便于问题排查
 * 
 * 异常处理优先级：
 * 1. 业务异常（ServiceException）
 * 2. 参数校验异常（BindException、MethodArgumentNotValidException）
 * 3. 请求方法异常（HttpRequestMethodNotSupportedException）
 * 4. 其他运行时异常（RuntimeException）
 * 5. 系统异常（Exception）
 * 
 * 使用场景：
 * - Controller 中抛出的任何异常都会被捕获
 * - 自动转换为统一的 JSON 响应
 * - 避免在每个方法中写 try-catch
 * 
 * @author hrone
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * 业务异常处理
     * 
     * 使用场景：
     * Service层抛出的业务异常
     * 
     * 示例：
     * throw new ServiceException("用户不存在");
     * throw new ServiceException("权限不足", 403);
     */
    @ExceptionHandler(ServiceException.class)
    public AjaxResult handleServiceException(ServiceException e) {
        // 记录日志
        log.error("业务异常：{}", e.getMessage());
        
        // 获取错误码，默认500
        Integer code = e.getCode();
        if (code == null) {
            code = HttpStatus.ERROR;
        }
        
        // 返回错误响应
        return AjaxResult.error(code, e.getMessage());
    }
    
    /**
     * 参数校验异常处理（绑定异常）
     * 
     * 使用场景：
     * - @Valid 校验失败
     * - 表单参数绑定失败
     * 
     * 示例：
     * public AjaxResult add(@Valid @RequestBody User user)
     */
    @ExceptionHandler(BindException.class)
    public AjaxResult handleBindException(BindException e) {
        log.error("参数校验异常：{}", e.getMessage());
        
        // 获取第一个校验失败的错误信息
        String message = e.getAllErrors().get(0).getDefaultMessage();
        
        return AjaxResult.error(HttpStatus.BAD_REQUEST, message);
    }
    
    /**
     * 参数校验异常处理（方法参数校验）
     * 
     * 使用场景：
     * - @RequestBody @Valid 校验失败
     * - JSON参数校验失败
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public AjaxResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("方法参数校验异常：{}", e.getMessage());
        
        // 获取第一个校验失败的错误信息
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        
        return AjaxResult.error(HttpStatus.BAD_REQUEST, message);
    }
    
    /**
     * 请求方法不支持异常处理
     * 
     * 使用场景：
     * - GET 方法访问 POST 接口
     * - POST 方法访问 GET 接口
     * 
     * 示例：
     * 接口定义为 @PostMapping，但用 GET 请求访问
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public AjaxResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        log.error("请求方法不支持：{}", e.getMessage());
        
        String message = StringUtils.format("请求方法 {} 不支持，支持的方法：{}", 
            e.getMethod(), 
            StringUtils.join(e.getSupportedHttpMethods(), ", ")
        );
        
        return AjaxResult.error(HttpStatus.BAD_METHOD, message);
    }
    
    /**
     * 运行时异常处理
     * 
     * 使用场景：
     * - 空指针异常（NullPointerException）
     * - 数组越界异常（IndexOutOfBoundsException）
     * - 类型转换异常（ClassCastException）
     * - 算术异常（ArithmeticException）
     */
    @ExceptionHandler(RuntimeException.class)
    public AjaxResult handleRuntimeException(RuntimeException e) {
        log.error("运行时异常：", e);
        
        // 获取异常类型
        String exceptionName = e.getClass().getSimpleName();
        
        // 构造错误消息
        String message = StringUtils.format("系统运行异常：{}，请联系管理员", exceptionName);
        
        return AjaxResult.error(HttpStatus.ERROR, message);
    }
    
    /**
     * 系统异常处理（兜底异常）
     * 
     * 使用场景：
     * - 所有未被上面方法捕获的异常
     * - 数据库异常
     * - IO异常
     * - 网络异常等
     */
    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e) {
        log.error("系统异常：", e);
        
        // 生产环境不应该暴露详细的异常信息
        // 返回统一的错误提示
        return AjaxResult.error(HttpStatus.ERROR, "系统内部错误，请联系管理员");
    }
}


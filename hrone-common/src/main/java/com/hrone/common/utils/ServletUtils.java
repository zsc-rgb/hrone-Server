package com.hrone.common.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet工具类
 * 
 * 功能说明：
 * 1. 快速获取 Request、Response、Session 对象
 * 2. 提供请求参数获取方法
 * 3. 提供响应输出方法
 * 
 * 技术要点：
 * - 使用 Spring 的 RequestContextHolder 获取请求上下文
 * - 无需在 Controller 方法参数中传递 Request/Response
 * - 线程安全，每个请求独立的上下文
 * 
 * @author hrone
 */
public class ServletUtils {
    
    /**
     * 获取 ServletRequestAttributes
     * 
     * 这是 Spring 提供的线程安全方式获取请求上下文
     */
    private static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }
    
    /**
     * 获取当前请求的 HttpServletRequest 对象
     * 
     * 使用场景：在 Service 层或工具类中需要获取请求对象
     * 
     * @return HttpServletRequest 对象
     * 
     * 示例：
     * HttpServletRequest request = ServletUtils.getRequest();
     * String ip = request.getRemoteAddr();
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }
    
    /**
     * 获取当前请求的 HttpServletResponse 对象
     * 
     * 使用场景：在 Service 层或工具类中需要设置响应
     * 
     * @return HttpServletResponse 对象
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes attributes = getRequestAttributes();
        return attributes != null ? attributes.getResponse() : null;
    }
    
    /**
     * 获取当前请求的 HttpSession 对象
     * 
     * 使用场景：需要在 Session 中存储数据
     * 注意：使用 JWT 的项目通常不使用 Session
     * 
     * @return HttpSession 对象
     */
    public static HttpSession getSession() {
        HttpServletRequest request = getRequest();
        return request != null ? request.getSession() : null;
    }
    
    /**
     * 获取请求参数（String类型）
     * 
     * 使用场景：快速获取请求参数
     * 
     * @param name 参数名
     * @return 参数值
     * 
     * 示例：
     * String username = ServletUtils.getParameter("username");
     */
    public static String getParameter(String name) {
        HttpServletRequest request = getRequest();
        return request != null ? request.getParameter(name) : null;
    }
    
    /**
     * 获取请求参数（String类型，带默认值）
     * 
     * @param name 参数名
     * @param defaultValue 默认值
     * @return 参数值
     * 
     * 示例：
     * String pageNum = ServletUtils.getParameter("pageNum", "1");
     */
    public static String getParameter(String name, String defaultValue) {
        String value = getParameter(name);
        return StringUtils.isNotEmpty(value) ? value : defaultValue;
    }
    
    /**
     * 获取请求参数（Integer类型）
     * 
     * 使用场景：获取数字类型参数，如分页参数
     * 
     * @param name 参数名
     * @return 参数值
     * 
     * 示例：
     * Integer pageNum = ServletUtils.getParameterToInt("pageNum");
     */
    public static Integer getParameterToInt(String name) {
        String value = getParameter(name);
        if (StringUtils.isNotEmpty(value)) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
    /**
     * 获取请求参数（Integer类型，带默认值）
     * 
     * @param name 参数名
     * @param defaultValue 默认值
     * @return 参数值
     * 
     * 示例：
     * Integer pageNum = ServletUtils.getParameterToInt("pageNum", 1);
     */
    public static Integer getParameterToInt(String name, Integer defaultValue) {
        Integer value = getParameterToInt(name);
        return value != null ? value : defaultValue;
    }
    
    /**
     * 获取请求参数（Boolean类型）
     * 
     * @param name 参数名
     * @return 参数值
     */
    public static Boolean getParameterToBool(String name) {
        String value = getParameter(name);
        if (StringUtils.isNotEmpty(value)) {
            return Boolean.parseBoolean(value);
        }
        return null;
    }
    
    /**
     * 获取请求参数（Boolean类型，带默认值）
     * 
     * @param name 参数名
     * @param defaultValue 默认值
     * @return 参数值
     */
    public static Boolean getParameterToBool(String name, Boolean defaultValue) {
        Boolean value = getParameterToBool(name);
        return value != null ? value : defaultValue;
    }
    
    /**
     * 获取请求的完整URL
     * 
     * 使用场景：记录访问日志、分享链接生成
     * 
     * @return 完整URL
     * 
     * 示例：
     * getRequestURL() = "http://localhost:8080/user/list?pageNum=1"
     */
    public static String getRequestURL() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        StringBuffer url = request.getRequestURL();
        String queryString = request.getQueryString();
        if (StringUtils.isNotEmpty(queryString)) {
            url.append("?").append(queryString);
        }
        return url.toString();
    }
    
    /**
     * 获取客户端IP地址
     * 
     * 使用场景：记录访问日志、安全控制
     * 
     * 说明：
     * - 考虑了代理和负载均衡的情况
     * - 优先从 X-Forwarded-For 等头部获取真实IP
     * 
     * @return 客户端IP地址
     * 
     * 示例：
     * getClientIP() = "192.168.1.100"
     */
    public static String getClientIP() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个IP才是真实IP
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        
        ip = request.getHeader("Proxy-Client-IP");
        if (StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        
        return request.getRemoteAddr();
    }
    
    /**
     * 获取请求方法（GET、POST等）
     * 
     * @return 请求方法
     * 
     * 示例：
     * getMethod() = "GET"
     */
    public static String getMethod() {
        HttpServletRequest request = getRequest();
        return request != null ? request.getMethod() : null;
    }
    
    /**
     * 判断是否为Ajax请求
     * 
     * 使用场景：区分普通请求和Ajax请求，返回不同的响应
     * 
     * @return true=Ajax请求，false=普通请求
     */
    public static boolean isAjaxRequest() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return false;
        }
        String requestedWith = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equalsIgnoreCase(requestedWith);
    }
    
    /**
     * 获取 User-Agent
     * 
     * 使用场景：判断浏览器类型、设备类型
     * 
     * @return User-Agent 字符串
     */
    public static String getUserAgent() {
        HttpServletRequest request = getRequest();
        return request != null ? request.getHeader("User-Agent") : null;
    }
    
    /**
     * 渲染字符串到客户端
     * 
     * 使用场景：直接输出JSON、XML等字符串
     * 
     * @param response 响应对象
     * @param content 内容
     * @param contentType 内容类型
     * 
     * 示例：
     * renderString(response, "{\"code\":200}", "application/json");
     */
    public static void renderString(HttpServletResponse response, String content, String contentType) {
        if (response == null) {
            return;
        }
        try {
            response.setStatus(200);
            response.setContentType(contentType);
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 渲染JSON字符串到客户端
     * 
     * 使用场景：拦截器、过滤器中返回JSON
     * 
     * @param response 响应对象
     * @param jsonString JSON字符串
     */
    public static void renderJson(HttpServletResponse response, String jsonString) {
        renderString(response, jsonString, "application/json");
    }
    
    /**
     * 设置响应头
     * 
     * @param name 头名称
     * @param value 头值
     * 
     * 示例：
     * setHeader("Access-Control-Allow-Origin", "*");
     */
    public static void setHeader(String name, String value) {
        HttpServletResponse response = getResponse();
        if (response != null) {
            response.setHeader(name, value);
        }
    }
}


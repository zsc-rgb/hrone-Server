package com.hrone.common.utils;

import java.util.Collection;
import java.util.Map;

/**
 * 字符串工具类
 * 
 * 功能说明：
 * 1. 提供常用的字符串判空、格式化等操作
 * 2. 补充 Apache Commons 和 Hutool 未提供的实用方法
 * 3. 统一字符串处理规范
 * 
 * @author hrone
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    
    /** 空字符串 */
    private static final String NULLSTR = "";
    
    /** 下划线 */
    private static final char SEPARATOR = '_';
    
    /**
     * 判断字符串是否为空
     * 
     * 使用场景：最常用的判空方法
     * 
     * @param str 待判断的字符串
     * @return true=为空，false=不为空
     * 
     * 示例：
     * isEmpty(null)      = true
     * isEmpty("")        = true
     * isEmpty(" ")       = false
     * isEmpty("hello")   = false
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }
    
    /**
     * 判断字符串是否不为空
     * 
     * @param str 待判断的字符串
     * @return true=不为空，false=为空
     * 
     * 示例：
     * isNotEmpty(null)      = false
     * isNotEmpty("")        = false
     * isNotEmpty(" ")       = true
     * isNotEmpty("hello")   = true
     */
    public static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }
    
    /**
     * 判断字符串是否为空或只包含空白字符
     * 
     * 使用场景：需要过滤空格、制表符、换行符等空白字符时
     * 
     * @param str 待判断的字符串
     * @return true=为空白，false=不为空白
     * 
     * 示例：
     * isBlank(null)      = true
     * isBlank("")        = true
     * isBlank(" ")       = true
     * isBlank("  \t\n")  = true
     * isBlank("hello")   = false
     */
    public static boolean isBlank(CharSequence str) {
        int length;
        if (str == null || (length = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 判断字符串是否不为空白
     * 
     * @param str 待判断的字符串
     * @return true=不为空白，false=为空白
     */
    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }
    
    /**
     * 去除字符串两端的空白字符
     * 
     * 使用场景：用户输入处理、数据清洗
     * 
     * @param str 待处理的字符串
     * @return 去除空白后的字符串
     * 
     * 示例：
     * trim(null)          = null
     * trim("")            = ""
     * trim("  hello  ")   = "hello"
     * trim("  hello\t\n") = "hello"
     */
    public static String trim(String str) {
        return (str == null ? null : str.trim());
    }
    
    /**
     * 截取字符串
     * 
     * @param str 字符串
     * @param start 开始位置
     * @return 截取后的字符串
     */
    public static String substring(final String str, int start) {
        if (str == null) {
            return NULLSTR;
        }
        
        if (start < 0) {
            start = str.length() + start;
        }
        
        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return NULLSTR;
        }
        
        return str.substring(start);
    }
    
    /**
     * 截取字符串
     * 
     * @param str 字符串
     * @param start 开始位置
     * @param end 结束位置
     * @return 截取后的字符串
     */
    public static String substring(final String str, int start, int end) {
        if (str == null) {
            return NULLSTR;
        }
        
        if (end < 0) {
            end = str.length() + end;
        }
        if (start < 0) {
            start = str.length() + start;
        }
        
        if (end > str.length()) {
            end = str.length();
        }
        
        if (start > end) {
            return NULLSTR;
        }
        
        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }
        
        return str.substring(start, end);
    }
    
    /**
     * 格式化字符串（占位符替换）
     * 
     * 使用场景：动态拼接字符串，比 String.format 更简洁
     * 
     * @param template 字符串模板
     * @param params 参数数组
     * @return 格式化后的字符串
     * 
     * 示例：
     * format("用户{}登录成功", "张三")              = "用户张三登录成功"
     * format("{}上传了{}个文件", "李四", 5)         = "李四上传了5个文件"
     * format("IP:{}在{}访问了系统", "127.0.0.1", "14:30") = "IP:127.0.0.1在14:30访问了系统"
     */
    public static String format(String template, Object... params) {
        if (isEmpty(template) || params == null || params.length == 0) {
            return template;
        }
        StringBuilder result = new StringBuilder(template.length() + 50);
        int handledPosition = 0;
        int delimIndex;
        for (Object param : params) {
            delimIndex = template.indexOf("{}", handledPosition);
            if (delimIndex == -1) {
                break;
            }
            result.append(template, handledPosition, delimIndex);
            result.append(param);
            handledPosition = delimIndex + 2;
        }
        result.append(template.substring(handledPosition));
        return result.toString();
    }
    
    /**
     * 判断集合是否为空
     * 
     * @param coll 集合
     * @return true=为空，false=不为空
     */
    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }
    
    /**
     * 判断集合是否不为空
     * 
     * @param coll 集合
     * @return true=不为空，false=为空
     */
    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }
    
    /**
     * 判断Map是否为空
     * 
     * @param map Map对象
     * @return true=为空，false=不为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
    
    /**
     * 判断Map是否不为空
     * 
     * @param map Map对象
     * @return true=不为空，false=为空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }
    
    /**
     * 判断对象数组是否为空
     * 
     * @param objects 对象数组
     * @return true=为空，false=不为空
     */
    public static boolean isEmpty(Object[] objects) {
        return objects == null || objects.length == 0;
    }
    
    /**
     * 判断对象数组是否不为空
     * 
     * @param objects 对象数组
     * @return true=不为空，false=为空
     */
    public static boolean isNotEmpty(Object[] objects) {
        return !isEmpty(objects);
    }
    
    /**
     * 驼峰转下划线命名
     * 
     * 使用场景：Java字段名转数据库列名
     * 
     * @param str 驼峰字符串
     * @return 下划线字符串
     * 
     * 示例：
     * toUnderScoreCase("userName")     = "user_name"
     * toUnderScoreCase("userId")       = "user_id"
     * toUnderScoreCase("createTime")   = "create_time"
     */
    public static String toUnderScoreCase(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean preCharIsUpperCase = true;
        boolean curCharIsUpperCase = true;
        boolean nextCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (i > 0) {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            } else {
                preCharIsUpperCase = false;
            }
            
            curCharIsUpperCase = Character.isUpperCase(c);
            
            if (i < (str.length() - 1)) {
                nextCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            } else {
                nextCharIsUpperCase = false;
            }
            
            if (preCharIsUpperCase && curCharIsUpperCase && !nextCharIsUpperCase) {
                sb.append(SEPARATOR);
            } else if ((i != 0 && !preCharIsUpperCase) && curCharIsUpperCase) {
                sb.append(SEPARATOR);
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }
    
    /**
     * 判断是否包含字符串（忽略大小写）
     * 
     * @param str 验证字符串
     * @param strs 字符串数组
     * @return 是否包含
     */
    public static boolean inStringIgnoreCase(String str, String... strs) {
        if (str != null && strs != null) {
            for (String s : strs) {
                if (str.equalsIgnoreCase(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * 判断对象是否为数组类型（Java基本类型数组）
     * 
     * @param object 对象
     * @return true=是数组，false=不是数组
     */
    public static boolean isArray(Object object) {
        return object != null && object.getClass().isArray();
    }
}


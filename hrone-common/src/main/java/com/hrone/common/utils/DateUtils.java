package com.hrone.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期工具类
 * 
 * 功能说明：
 * 1. 提供常用的日期格式化和解析方法
 * 2. 支持 Date 和 LocalDateTime 的互转
 * 3. 提供日期计算功能
 * 
 * 技术要点：
 * - 使用 Java 8 的 LocalDateTime 代替旧的 Date
 * - 线程安全的日期格式化
 * - 统一日期格式规范
 * 
 * @author hrone
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    
    /**
     * 日期格式常量
     * 在项目中统一使用这些格式，避免到处写格式字符串
     */
    public static final String YYYY = "yyyy";
    public static final String YYYY_MM = "yyyy-MM";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * DateTimeFormatter 是线程安全的，可以声明为静态变量
     */
    private static final DateTimeFormatter FORMATTER_YYYY_MM_DD = DateTimeFormatter.ofPattern(YYYY_MM_DD);
    private static final DateTimeFormatter FORMATTER_YYYY_MM_DD_HH_MM_SS = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
    
    /**
     * 获取当前日期时间（Date类型）
     * 
     * 使用场景：创建时间、更新时间等
     * 
     * @return 当前日期时间
     * 
     * 示例：
     * getNow() = 2025-11-05 14:30:00
     */
    public static Date getNow() {
        return new Date();
    }
    
    /**
     * 获取当前日期（LocalDate类型）
     * 
     * 使用场景：只需要日期，不需要时间的场合
     * 
     * @return 当前日期
     * 
     * 示例：
     * getLocalDate() = 2025-11-05
     */
    public static LocalDate getLocalDate() {
        return LocalDate.now();
    }
    
    /**
     * 获取当前日期时间（LocalDateTime类型）
     * 
     * 使用场景：推荐使用，Java 8+ 新特性
     * 
     * @return 当前日期时间
     * 
     * 示例：
     * getLocalDateTime() = 2025-11-05T14:30:00
     */
    public static LocalDateTime getLocalDateTime() {
        return LocalDateTime.now();
    }
    
    /**
     * 格式化日期为字符串（默认格式：yyyy-MM-dd HH:mm:ss）
     * 
     * 使用场景：最常用的日期格式化
     * 
     * @param date 日期
     * @return 格式化后的字符串
     * 
     * 示例：
     * formatDateTime(new Date()) = "2025-11-05 14:30:00"
     */
    public static String formatDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return formatDateTime(toLocalDateTime(date));
    }
    
    /**
     * 格式化日期为字符串（默认格式：yyyy-MM-dd HH:mm:ss）
     * 
     * @param dateTime LocalDateTime
     * @return 格式化后的字符串
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return FORMATTER_YYYY_MM_DD_HH_MM_SS.format(dateTime);
    }
    
    /**
     * 格式化日期为字符串（格式：yyyy-MM-dd）
     * 
     * 使用场景：只需要显示日期，不需要时间
     * 
     * @param date 日期
     * @return 格式化后的字符串
     * 
     * 示例：
     * formatDate(new Date()) = "2025-11-05"
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        return formatDate(toLocalDate(date));
    }
    
    /**
     * 格式化日期为字符串（格式：yyyy-MM-dd）
     * 
     * @param date LocalDate
     * @return 格式化后的字符串
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return FORMATTER_YYYY_MM_DD.format(date);
    }
    
    /**
     * 按照指定格式格式化日期
     * 
     * 使用场景：需要自定义日期格式时
     * 
     * @param date 日期
     * @param pattern 格式模式
     * @return 格式化后的字符串
     * 
     * 示例：
     * format(new Date(), "yyyy年MM月dd日") = "2025年11月05日"
     * format(new Date(), "yyyyMMdd")       = "20251105"
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
    
    /**
     * 按照指定格式格式化日期
     * 
     * @param dateTime LocalDateTime
     * @param pattern 格式模式
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(dateTime);
    }
    
    /**
     * 解析字符串为日期（格式：yyyy-MM-dd HH:mm:ss）
     * 
     * 使用场景：前端传来的日期字符串转换
     * 
     * @param dateStr 日期字符串
     * @return Date对象
     * 
     * 示例：
     * parseDateTime("2025-11-05 14:30:00") = Date对象
     */
    public static Date parseDateTime(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
    
    /**
     * 解析字符串为日期（格式：yyyy-MM-dd）
     * 
     * @param dateStr 日期字符串
     * @return Date对象
     * 
     * 示例：
     * parseDate("2025-11-05") = Date对象
     */
    public static Date parseDate(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
    
    /**
     * 按照指定格式解析字符串为日期
     * 
     * @param dateStr 日期字符串
     * @param pattern 格式模式
     * @return Date对象
     * 
     * 示例：
     * parse("2025年11月05日", "yyyy年MM月dd日") = Date对象
     */
    public static Date parse(String dateStr, String pattern) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
    
    /**
     * 解析字符串为 LocalDateTime（格式：yyyy-MM-dd HH:mm:ss）
     * 
     * @param dateStr 日期字符串
     * @return LocalDateTime对象
     */
    public static LocalDateTime parseLocalDateTime(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        return LocalDateTime.parse(dateStr, FORMATTER_YYYY_MM_DD_HH_MM_SS);
    }
    
    /**
     * 解析字符串为 LocalDate（格式：yyyy-MM-dd）
     * 
     * @param dateStr 日期字符串
     * @return LocalDate对象
     */
    public static LocalDate parseLocalDate(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        return LocalDate.parse(dateStr, FORMATTER_YYYY_MM_DD);
    }
    
    /**
     * Date 转 LocalDateTime
     * 
     * 使用场景：旧代码兼容，将 Date 转为推荐的 LocalDateTime
     * 
     * @param date Date对象
     * @return LocalDateTime对象
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    
    /**
     * Date 转 LocalDate
     * 
     * @param date Date对象
     * @return LocalDate对象
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    
    /**
     * LocalDateTime 转 Date
     * 
     * 使用场景：需要兼容使用 Date 的旧代码
     * 
     * @param dateTime LocalDateTime对象
     * @return Date对象
     */
    public static Date toDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    
    /**
     * LocalDate 转 Date
     * 
     * @param date LocalDate对象
     * @return Date对象
     */
    public static Date toDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    
    /**
     * 获取当前日期的开始时间（00:00:00）
     * 
     * 使用场景：查询某一天的数据
     * 
     * @return 当天开始时间
     * 
     * 示例：
     * getStartOfDay() = 2025-11-05 00:00:00
     */
    public static Date getStartOfDay() {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        return toDate(startOfDay);
    }
    
    /**
     * 获取当前日期的结束时间（23:59:59）
     * 
     * 使用场景：查询某一天的数据
     * 
     * @return 当天结束时间
     * 
     * 示例：
     * getEndOfDay() = 2025-11-05 23:59:59
     */
    public static Date getEndOfDay() {
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return toDate(endOfDay);
    }
    
    /**
     * 计算两个日期之间相差的天数
     * 
     * 使用场景：计算会员剩余天数、借阅天数等
     * 
     * @param start 开始日期
     * @param end 结束日期
     * @return 相差天数
     * 
     * 示例：
     * daysBetween(2025-11-01, 2025-11-05) = 4
     */
    public static long daysBetween(Date start, Date end) {
        if (start == null || end == null) {
            return 0;
        }
        LocalDate startDate = toLocalDate(start);
        LocalDate endDate = toLocalDate(end);
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
    }
    
    /**
     * 给日期增加指定天数
     * 
     * 使用场景：计算会员到期日、截止日期等
     * 
     * @param date 原日期
     * @param days 要增加的天数（负数表示减少）
     * @return 计算后的日期
     * 
     * 示例：
     * addDays(2025-11-05, 7)  = 2025-11-12
     * addDays(2025-11-05, -2) = 2025-11-03
     */
    public static Date addDays(Date date, int days) {
        if (date == null) {
            return null;
        }
        LocalDateTime dateTime = toLocalDateTime(date).plusDays(days);
        return toDate(dateTime);
    }
    
    /**
     * 给日期增加指定月数
     * 
     * @param date 原日期
     * @param months 要增加的月数
     * @return 计算后的日期
     */
    public static Date addMonths(Date date, int months) {
        if (date == null) {
            return null;
        }
        LocalDateTime dateTime = toLocalDateTime(date).plusMonths(months);
        return toDate(dateTime);
    }
    
    /**
     * 给日期增加指定年数
     * 
     * @param date 原日期
     * @param years 要增加的年数
     * @return 计算后的日期
     */
    public static Date addYears(Date date, int years) {
        if (date == null) {
            return null;
        }
        LocalDateTime dateTime = toLocalDateTime(date).plusYears(years);
        return toDate(dateTime);
    }
}


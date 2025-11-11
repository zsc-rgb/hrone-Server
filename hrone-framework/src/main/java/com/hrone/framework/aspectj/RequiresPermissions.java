package com.hrone.framework.aspectj;

import java.lang.annotation.*;

/**
 * 权限注解（第6阶段 6.4）
 *
 * 用法：
 *  @RequiresPermissions({"system:user:list"})
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresPermissions {
	String[] value() default {};
}



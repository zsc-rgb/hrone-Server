package com.hrone.common.annotation;

import com.hrone.common.enums.BusinessType;
import com.hrone.common.enums.OperatorType;

import java.lang.annotation.*;

/**
 * 操作日志记录注解（第8阶段）
 *
 * 用于在Controller或Service方法上标注需要记录的业务操作。
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperLog {

	/**
	 * 模块标题
	 */
	String title() default "";

	/**
	 * 业务类型
	 */
	BusinessType businessType() default BusinessType.OTHER;

	/**
	 * 操作人类别
	 */
	OperatorType operatorType() default OperatorType.MANAGE;
}


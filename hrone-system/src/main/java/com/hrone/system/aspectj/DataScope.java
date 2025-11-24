 package com.hrone.system.aspectj;

import java.lang.annotation.*;

/**
 * 数据权限注解（第7阶段）
 *
 * 演示规则：
 * - 角色 data_scope = '1' 全部数据：不做限制
 * - 角色 data_scope = '3' 本部门：按当前用户 deptId 过滤
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {
	String deptAlias() default "";
}



package com.hrone.framework.aspectj;

import com.hrone.common.exception.ServiceException;
import com.hrone.common.utils.ServletUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 简易权限切面（演示版）
 *
 * 说明：
 * - 从请求头 X-Perms 读取权限列表（以逗号分隔）
 * - 检查是否包含注解声明的权限
 * - 用于演示注解式权限控制的实现思路
 */
@Aspect
@Component
public class PermissionAspect {

	@Before("@annotation(req)")
	public void checkPermission(JoinPoint joinPoint, RequiresPermissions req) {
		String[] needed = req.value();
		if (needed == null || needed.length == 0) {
			return;
		}
		HttpServletRequest request = ServletUtils.getRequest();
		if (request == null) {
			throw new ServiceException("无法获取请求对象，权限校验失败", 500);
		}
		String permsHeader = request.getHeader("X-Perms");
		if (permsHeader == null || permsHeader.trim().isEmpty()) {
			throw new ServiceException("权限不足（缺少必要权限）", 403);
		}
		Set<String> have = new HashSet<>(Arrays.asList(permsHeader.split(",")));
		for (String p : needed) {
			if (!have.contains(p)) {
				throw new ServiceException("权限不足（缺少：" + p + "）", 403);
			}
		}
	}
}



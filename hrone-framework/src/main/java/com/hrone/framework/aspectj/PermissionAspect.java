package com.hrone.framework.aspectj;

import com.hrone.common.constant.Constants;
import com.hrone.common.exception.ServiceException;
import com.hrone.common.utils.ServletUtils;
import com.hrone.common.utils.StringUtils;
import com.hrone.system.domain.SysMenu;
import com.hrone.system.service.ISysMenuService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限切面（第7阶段：RBAC演示）
 *
 * 校验顺序：
 * 1) 若已登录（过滤器写入 login_user_key），则从数据库加载该用户的权限集合进行校验
 * 2) 若未登录或未取到权限集合，回退到请求头 X-Perms（用于本地快速演示）
 */
@Aspect
@Component
public class PermissionAspect {

	private static final String REQUEST_PERMS_KEY = "login_user_perms";

	@Autowired
	private ISysMenuService menuService;

	@Before("@annotation(req)")
	public void checkPermission(JoinPoint
											joinPoint, RequiresPermissions req) {
		String[] needed = req.value();
		if (needed == null || needed.length == 0) {
			return;
		}
		HttpServletRequest request = ServletUtils.getRequest();
		if (request == null) {
			throw new ServiceException("无法获取请求对象，权限校验失败", 500);
		}

		Object uid = request.getAttribute(Constants.LOGIN_USER_KEY);
		if (uid == null) {
			throw new ServiceException("未认证，无法进行权限校验", 401);
		}

		Set<String> have = getOrLoadPermissions(request, uid);

		// 最终校验
		for (String p : needed) {
			if (!have.contains(p)) {
				throw new ServiceException("权限不足（缺少：" + p + "）", 403);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Set<String> getOrLoadPermissions(HttpServletRequest request, Object uid) {
		Object cached = request.getAttribute(REQUEST_PERMS_KEY);
		if (cached instanceof Set) {
			return (Set<String>) cached;
		}

		Long userId = Long.valueOf(String.valueOf(uid));
		Set<String> perms = new HashSet<>();
		List<SysMenu> menus = menuService.selectMenusByUserId(userId);
		if (menus != null) {
			perms.addAll(menus.stream()
				.map(SysMenu::getPerms)
				.filter(StringUtils::isNotEmpty)
				.flatMap(p -> java.util.Arrays.stream(p.split(",")))
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.collect(Collectors.toSet()));
		}

		Set<String> immutable = Collections.unmodifiableSet(perms);
		request.setAttribute(REQUEST_PERMS_KEY, immutable);
		return immutable;
	}
}



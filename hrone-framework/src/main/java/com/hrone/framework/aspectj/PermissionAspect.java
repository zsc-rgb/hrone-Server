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
import java.util.*;
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

		// 1) 尝试从登录上下文中获取用户ID，并根据用户ID加载权限集合
		Set<String> have = new HashSet<>();
		Object uid = request.getAttribute(Constants.LOGIN_USER_KEY);
		if (uid != null) {
			try {
				Long userId = Long.valueOf(String.valueOf(uid));
				// 简化：当前 ISysMenuService.selectMenusByUserId 返回“所有菜单”（演示阶段）
				// 我们从菜单中抽取 perms 作为权限集合
				List<SysMenu> menus = menuService.selectMenusByUserId(userId);
				have.addAll(menus.stream()
					.map(SysMenu::getPerms)
					.filter(StringUtils::isNotEmpty)
					.flatMap(p -> Arrays.stream(p.split(",")))
					.map(String::trim)
					.filter(s -> !s.isEmpty())
					.collect(Collectors.toSet()));
			} catch (Exception ignore) {
				// 忽略异常，回退到 header
			}
		}

		// 2) 回退：从请求头 X-Perms 读取权限（用于本地演示或未登录场景）
		if (have.isEmpty()) {
			String permsHeader = request.getHeader("X-Perms");
			if (StringUtils.isNotEmpty(permsHeader)) {
				have.addAll(Arrays.stream(permsHeader.split(","))
					.map(String::trim)
					.filter(s -> !s.isEmpty())
					.collect(Collectors.toSet()));
			}
		}

		// 最终校验
		for (String p : needed) {
			if (!have.contains(p)) {
				throw new ServiceException("权限不足（缺少：" + p + "）", 403);
			}
		}
	}
}



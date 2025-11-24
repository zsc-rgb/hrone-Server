package com.hrone.system.aspectj;

import com.hrone.common.constant.Constants;
import com.hrone.common.constant.UserConstants;
import com.hrone.common.exception.ServiceException;
import com.hrone.system.domain.SysRole;
import com.hrone.system.domain.SysUser;
import com.hrone.system.mapper.SysRoleDeptMapper;
import com.hrone.system.service.ISysDeptService;
import com.hrone.system.service.ISysRoleService;
import com.hrone.system.service.ISysUserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
public class DataScopeAspect {

	@Autowired
	private ISysUserService userService;

	@Autowired
	private ISysRoleService roleService;

	@Autowired
	private ISysDeptService deptService;

	@Autowired
	private SysRoleDeptMapper roleDeptMapper;

	@Autowired
	private HttpServletRequest request;

	@Before("@annotation(com.hrone.system.aspectj.DataScope)")
	public void before(JoinPoint joinPoint) {
		Object uid = request.getAttribute(Constants.LOGIN_USER_KEY);
		if (uid == null) {
			return;
		}
		Long userId = Long.valueOf(String.valueOf(uid));
		SysUser user = userService.selectUserById(userId);
		if (user == null) {
			throw new ServiceException("用户不存在，无法进行数据权限过滤", 401);
		}
		List<SysRole> roles = roleService.selectRolesByUserId(userId);
		if (roles == null || roles.isEmpty()) {
			DataScopeContext.setSelfScope(userId);
			return;
		}

		// 1) 全部数据：直接放行
		boolean allData = roles.stream()
			.anyMatch(r -> UserConstants.DATA_SCOPE_ALL.equals(r.getDataScope()));
		if (allData) {
			DataScopeContext.clear();
			return;
		}

		Set<Long> deptIds = new HashSet<>();

		// 2) 自定义数据权限（角色绑定的部门）
		List<Long> customRoleIds = roles.stream()
			.filter(r -> UserConstants.DATA_SCOPE_CUSTOM.equals(r.getDataScope()))
			.map(SysRole::getRoleId)
			.collect(Collectors.toList());
		if (!customRoleIds.isEmpty()) {
			List<Long> customDeptIds = roleDeptMapper.selectDeptIdsByRoleIds(customRoleIds);
			if (customDeptIds != null) {
				deptIds.addAll(customDeptIds);
			}
		}

		// 3) 本部门 / 本部门及以下
		Long deptId = user.getDeptId();
		if (deptId != null) {
			boolean deptOnly = roles.stream()
				.anyMatch(r -> UserConstants.DATA_SCOPE_DEPT.equals(r.getDataScope()));
			if (deptOnly) {
				deptIds.add(deptId);
			}

			boolean deptAndChild = roles.stream()
				.anyMatch(r -> UserConstants.DATA_SCOPE_DEPT_AND_CHILD.equals(r.getDataScope()));
			if (deptAndChild) {
				deptIds.add(deptId);
				List<Long> childDeptIds = deptService.selectChildDeptIds(deptId);
				if (childDeptIds != null) {
					deptIds.addAll(childDeptIds);
				}
			}
		}

		// 4) 仅本人
		boolean selfScope = roles.stream()
			.anyMatch(r -> UserConstants.DATA_SCOPE_SELF.equals(r.getDataScope()));

		if (!deptIds.isEmpty()) {
			DataScopeContext.setDeptIds(deptIds);
			DataScopeContext.setSelfScope(null);
		} else if (selfScope) {
			DataScopeContext.setSelfScope(userId);
			DataScopeContext.setDeptIds(null);
		} else {
			DataScopeContext.clear();
		}
	}

	@After("@annotation(com.hrone.system.aspectj.DataScope)")
	public void after(JoinPoint joinPoint) {
		DataScopeContext.clear();
	}
}



package com.hrone.system.aspectj;

/**
 * 数据权限上下文（ThreadLocal）
 *
 * 支持：
 * - 部门ID集合（自定义/本部门/本部门及以下）
 * - 仅本人数据范围
 */
public final class DataScopeContext {
	private static final ThreadLocal<java.util.Set<Long>> DEPT_IDS_HOLDER = new ThreadLocal<>();
	private static final ThreadLocal<Long> SELF_USER_HOLDER = new ThreadLocal<>();

	private DataScopeContext() {}

	public static void setDeptIds(java.util.Set<Long> deptIds) {
		if (deptIds == null || deptIds.isEmpty()) {
			DEPT_IDS_HOLDER.remove();
			return;
		}
		DEPT_IDS_HOLDER.set(deptIds);
	}

	public static java.util.Set<Long> getDeptIds() {
		return DEPT_IDS_HOLDER.get();
	}

	public static void setSelfScope(Long userId) {
		if (userId == null) {
			SELF_USER_HOLDER.remove();
		} else {
			SELF_USER_HOLDER.set(userId);
		}
	}

	public static boolean isSelfScope() {
		return SELF_USER_HOLDER.get() != null;
	}

	public static Long getSelfUserId() {
		return SELF_USER_HOLDER.get();
	}

	public static void clear() {
		DEPT_IDS_HOLDER.remove();
		SELF_USER_HOLDER.remove();
	}
}



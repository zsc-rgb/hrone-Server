package com.hrone.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 角色与部门关联 Mapper
 */
@Mapper
public interface SysRoleDeptMapper {

	/**
	 * 查询角色绑定的部门ID集合
	 *
	 * @param roleIds 角色ID集合
	 * @return 部门ID列表
	 */
	@Select({
		"<script>",
		"SELECT DISTINCT dept_id",
		"FROM sys_role_dept",
		"WHERE role_id IN",
		"<foreach collection='roleIds' item='id' open='(' separator=',' close=')'>",
		"#{id}",
		"</foreach>",
		"</script>"
	})
	List<Long> selectDeptIdsByRoleIds(@Param("roleIds") List<Long> roleIds);
}


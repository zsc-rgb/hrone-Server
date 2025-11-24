package com.hrone.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrone.system.domain.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 角色管理 Mapper接口
 * 
 * @author hrone
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
	/**
	 * 根据用户ID查询角色列表
	 */
	@Select("SELECT DISTINCT r.* " +
		"FROM sys_role r " +
		"JOIN sys_user_role ur ON ur.role_id = r.role_id " +
		"WHERE ur.user_id = #{userId} AND r.del_flag = '0'")
	List<SysRole> selectRolesByUserId(@Param("userId") Long userId);
}


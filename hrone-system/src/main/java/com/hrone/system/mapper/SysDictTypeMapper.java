package com.hrone.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrone.system.domain.SysDictType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysDictTypeMapper extends BaseMapper<SysDictType> {

	@Select("SELECT COUNT(1) FROM sys_dict_type WHERE dict_type = #{dictType} AND del_flag = '0' AND dict_id <> #{dictId}")
	int checkDictTypeUnique(@Param("dictType") String dictType, @Param("dictId") Long dictId);
}


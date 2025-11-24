package com.hrone.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrone.system.domain.SysDictData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysDictDataMapper extends BaseMapper<SysDictData> {

	@Select("SELECT * FROM sys_dict_data WHERE dict_type = #{dictType} AND status = '0' AND del_flag = '0' ORDER BY dict_sort ASC")
	List<SysDictData> selectDictDataByType(@Param("dictType") String dictType);
}


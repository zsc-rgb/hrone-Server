package com.hrone.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hrone.system.domain.SysDictData;
import com.hrone.system.domain.SysDictType;

import java.util.List;

public interface ISysDictTypeService extends IService<SysDictType> {

	List<SysDictType> selectDictTypeList(SysDictType dictType);

	SysDictType selectDictTypeById(Long dictId);

	SysDictType selectDictTypeByType(String dictType);

	List<SysDictData> selectDictDataByType(String dictType);

	int insertDictType(SysDictType dictType);

	int updateDictType(SysDictType dictType);

	int deleteDictTypeByIds(Long[] dictIds);

	boolean checkDictTypeUnique(SysDictType dictType);
}


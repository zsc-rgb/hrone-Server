package com.hrone.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hrone.system.domain.SysDictData;

import java.util.List;

public interface ISysDictDataService extends IService<SysDictData> {

	List<SysDictData> selectDictDataList(SysDictData dictData);

	SysDictData selectDictDataById(Long dictCode);

	int insertDictData(SysDictData dictData);

	int updateDictData(SysDictData dictData);

	int deleteDictDataByIds(Long[] dictCodes);
}


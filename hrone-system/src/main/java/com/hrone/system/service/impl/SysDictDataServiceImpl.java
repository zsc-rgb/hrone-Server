package com.hrone.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrone.common.exception.ServiceException;
import com.hrone.common.utils.StringUtils;
import com.hrone.system.domain.SysDictData;
import com.hrone.system.mapper.SysDictDataMapper;
import com.hrone.system.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements ISysDictDataService {

	@Autowired
	private SysDictDataMapper dictDataMapper;

	@Override
	public List<SysDictData> selectDictDataList(SysDictData dictData) {
		LambdaQueryWrapper<SysDictData> wrapper = new LambdaQueryWrapper<>();
		if (StringUtils.isNotEmpty(dictData.getDictType())) {
			wrapper.eq(SysDictData::getDictType, dictData.getDictType());
		}
		if (StringUtils.isNotEmpty(dictData.getDictLabel())) {
			wrapper.like(SysDictData::getDictLabel, dictData.getDictLabel());
		}
		if (StringUtils.isNotEmpty(dictData.getStatus())) {
			wrapper.eq(SysDictData::getStatus, dictData.getStatus());
		}
		wrapper.eq(SysDictData::getDelFlag, "0");
		wrapper.orderByAsc(SysDictData::getDictSort);
		return dictDataMapper.selectList(wrapper);
	}

	@Override
	public SysDictData selectDictDataById(Long dictCode) {
		return dictDataMapper.selectById(dictCode);
	}

	@Override
	public int insertDictData(SysDictData dictData) {
		return dictDataMapper.insert(dictData);
	}

	@Override
	public int updateDictData(SysDictData dictData) {
		if (dictData.getDictCode() == null) {
			throw new ServiceException("字典数据ID不能为空");
		}
		return dictDataMapper.updateById(dictData);
	}

	@Override
	public int deleteDictDataByIds(Long[] dictCodes) {
		return dictDataMapper.deleteBatchIds(Arrays.asList(dictCodes));
	}
}


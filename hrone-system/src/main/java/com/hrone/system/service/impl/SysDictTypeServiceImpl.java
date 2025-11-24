package com.hrone.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrone.common.exception.ServiceException;
import com.hrone.common.utils.StringUtils;
import com.hrone.system.domain.SysDictData;
import com.hrone.system.domain.SysDictType;
import com.hrone.system.mapper.SysDictDataMapper;
import com.hrone.system.mapper.SysDictTypeMapper;
import com.hrone.system.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements ISysDictTypeService {

	@Autowired
	private SysDictTypeMapper dictTypeMapper;

	@Autowired
	private SysDictDataMapper dictDataMapper;

	@Override
	public List<SysDictType> selectDictTypeList(SysDictType dictType) {
		LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();
		if (StringUtils.isNotEmpty(dictType.getDictName())) {
			wrapper.like(SysDictType::getDictName, dictType.getDictName());
		}
		if (StringUtils.isNotEmpty(dictType.getDictType())) {
			wrapper.eq(SysDictType::getDictType, dictType.getDictType());
		}
		if (StringUtils.isNotEmpty(dictType.getStatus())) {
			wrapper.eq(SysDictType::getStatus, dictType.getStatus());
		}
		wrapper.eq(SysDictType::getDelFlag, "0");
		wrapper.orderByAsc(SysDictType::getDictId);
		return dictTypeMapper.selectList(wrapper);
	}

	@Override
	public SysDictType selectDictTypeById(Long dictId) {
		return dictTypeMapper.selectById(dictId);
	}

	@Override
	public SysDictType selectDictTypeByType(String dictType) {
		LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SysDictType::getDictType, dictType);
		wrapper.eq(SysDictType::getDelFlag, "0");
		return dictTypeMapper.selectOne(wrapper);
	}

	@Override
	public List<SysDictData> selectDictDataByType(String dictType) {
		return dictDataMapper.selectDictDataByType(dictType);
	}

	@Override
	public int insertDictType(SysDictType dictType) {
		if (!checkDictTypeUnique(dictType)) {
			throw new ServiceException("字典类型已存在");
		}
		return dictTypeMapper.insert(dictType);
	}

	@Override
	public int updateDictType(SysDictType dictType) {
		if (!checkDictTypeUnique(dictType)) {
			throw new ServiceException("字典类型已存在");
		}
		return dictTypeMapper.updateById(dictType);
	}

	@Override
	public int deleteDictTypeByIds(Long[] dictIds) {
		for (Long dictId : dictIds) {
			SysDictType dictType = dictTypeMapper.selectById(dictId);
			if (dictType == null) {
				continue;
			}
			List<SysDictData> dataList = selectDictDataByType(dictType.getDictType());
			if (!dataList.isEmpty()) {
				throw new ServiceException("字典类型存在字典数据，无法删除");
			}
		}
		return dictTypeMapper.deleteBatchIds(java.util.Arrays.asList(dictIds));
	}

	@Override
	public boolean checkDictTypeUnique(SysDictType dictType) {
		Long dictId = dictType.getDictId() == null ? -1L : dictType.getDictId();
		return dictTypeMapper.checkDictTypeUnique(dictType.getDictType(), dictId) == 0;
	}
}


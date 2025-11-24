package com.hrone.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrone.common.utils.StringUtils;
import com.hrone.system.domain.SysOperLog;
import com.hrone.system.mapper.SysOperLogMapper;
import com.hrone.system.service.ISysOperLogService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements ISysOperLogService {

	@Override
	public void insertOperLog(SysOperLog operLog) {
		this.baseMapper.insert(operLog);
	}

	@Override
	public List<SysOperLog> selectOperLogList(SysOperLog operLog) {
		LambdaQueryWrapper<SysOperLog> wrapper = new LambdaQueryWrapper<>();
		if (StringUtils.isNotEmpty(operLog.getTitle())) {
			wrapper.like(SysOperLog::getTitle, operLog.getTitle());
		}
		if (operLog.getBusinessType() != null) {
			wrapper.eq(SysOperLog::getBusinessType, operLog.getBusinessType());
		}
		if (operLog.getStatus() != null) {
			wrapper.eq(SysOperLog::getStatus, operLog.getStatus());
		}
		wrapper.orderByDesc(SysOperLog::getOperTime);
		return this.baseMapper.selectList(wrapper);
	}

	@Override
	public int deleteOperLogByIds(Long[] operIds) {
		return this.baseMapper.deleteBatchIds(Arrays.asList(operIds));
	}

	@Override
	public void cleanOperLog() {
		this.lambdaUpdate().remove();
	}
}


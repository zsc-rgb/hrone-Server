package com.hrone.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrone.common.utils.StringUtils;
import com.hrone.system.domain.SysLoginLog;
import com.hrone.system.mapper.SysLoginLogMapper;
import com.hrone.system.service.ISysLoginLogService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements ISysLoginLogService {

	@Override
	public void insertLoginLog(SysLoginLog loginLog) {
		this.baseMapper.insert(loginLog);
	}

	@Override
	public List<SysLoginLog> selectLoginLogList(SysLoginLog loginLog) {
		LambdaQueryWrapper<SysLoginLog> wrapper = new LambdaQueryWrapper<>();
		if (StringUtils.isNotEmpty(loginLog.getUserName())) {
			wrapper.like(SysLoginLog::getUserName, loginLog.getUserName());
		}
		if (StringUtils.isNotEmpty(loginLog.getStatus())) {
			wrapper.eq(SysLoginLog::getStatus, loginLog.getStatus());
		}
		wrapper.orderByDesc(SysLoginLog::getLoginTime);
		return this.baseMapper.selectList(wrapper);
	}

	@Override
	public int deleteLoginLogByIds(Long[] infoIds) {
		return this.baseMapper.deleteBatchIds(Arrays.asList(infoIds));
	}

	@Override
	public void cleanLoginLog() {
		this.lambdaUpdate().remove();
	}
}


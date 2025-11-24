package com.hrone.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hrone.system.domain.SysOperLog;

import java.util.List;

public interface ISysOperLogService extends IService<SysOperLog> {

	void insertOperLog(SysOperLog operLog);

	List<SysOperLog> selectOperLogList(SysOperLog operLog);

	int deleteOperLogByIds(Long[] operIds);

	void cleanOperLog();
}


package com.hrone.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hrone.system.domain.SysLoginLog;

import java.util.List;

public interface ISysLoginLogService extends IService<SysLoginLog> {

	void insertLoginLog(SysLoginLog loginLog);

	List<SysLoginLog> selectLoginLogList(SysLoginLog loginLog);

	int deleteLoginLogByIds(Long[] infoIds);

	void cleanLoginLog();
}

